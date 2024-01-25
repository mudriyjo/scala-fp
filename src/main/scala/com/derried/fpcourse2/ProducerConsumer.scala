package com.derried.fpcourse2

import scala.collection.mutable
import scala.collection.mutable.Queue
import scala.util.Random

object ProducerConsumer extends App {

  class Wrapper {
    private var value: Int = 0
    def isEmpty: Boolean = value == 0
    def getValue: Int = {
      val result = value
      value = 0
      result
    }
    def setValue(x: Int) = value = x

  }

  def simpleProducerConsumer() = {
    val wrapper = new Wrapper
    val consumer = new Thread(() => {
      while(wrapper.isEmpty) {
        println("[consumer] actively waiting...")
      }
      println("[consumer] consume variable: " + wrapper.getValue)
    })

    val producer = new Thread(() => {
      println("[producer] Hard work started...")
      Thread.sleep(2000)
      val result = 42
      println("[producer] calculated value is: " + result)
      wrapper.setValue(result)
    })

    consumer.start()
    producer.start()
  }

//  simpleProducerConsumer()

  def smartProducerConsumer() = {
    val wrapper = new Wrapper
    val consumer = new Thread(() => {
      wrapper.synchronized {
        println("[consumer] waiting...")
        wrapper.wait()
      }
      println("[consumer] consume variable: " + wrapper.getValue)
    })

    val producer = new Thread(() => {
      println("[producer] Hard work started...")
      Thread.sleep(2000)
      val result = 42
      println("[producer] calculated value is: " + result)
      wrapper.synchronized {
        wrapper.setValue(result)
        wrapper.notify()
      }
    })

    consumer.start()
    producer.start()
    consumer.join()
    producer.join()
  }

//  smartProducerConsumer()

  /*
  producer -> [ ? ? ? ] -> consumer
   */

  class QueuePrCns(private val length: Int = 3) {
    private val queue: mutable.Queue[Int] = mutable.Queue()
    def isFull: Boolean = queue.synchronized {
      return queue.length == length
    }
    def isEmpty: Boolean = queue.synchronized(
      return queue.isEmpty
    )

    def addValue(x: Int) = queue.synchronized {
      if (this.isFull) {
        println("[producer] The queue is full, waiting ....")
        queue.wait()
      }
      queue.addOne(x)
      queue.notify()
    }

    def getValue: Int = queue.synchronized {
      if (this.isEmpty) {
        println("[consumer] The queue is empty, waiting ....")
        queue.wait()
      }
      val res = queue.dequeue()
      queue.notify()
      res
    }

    override def toString: String = s"Queue(${queue.mkString(",")})"
  }

  def queueProducerConsumer() = {
    val queue = new QueuePrCns()
    val consumer = new Thread(() => {
      (1 to 10).foreach(_ => {
        Thread.sleep(1500)
        println("[consumer] consume variable: " + queue.getValue)
      })
    })

    val producer = new Thread(() => {
      (1 to 10).foreach(_ => {
        println("[producer] Hard work started...")
        Thread.sleep(500)
        val result = Random.nextInt(42)
        println("[producer] calculated value is: " + result)
        queue.addValue(result)
      })
    })

    consumer.start()
    producer.start()
    consumer.join()
    producer.join()
  }

//  queueProducerConsumer()

/*
  producer 1 -> [ ? ? ? ] -> consumer1
  producer2 -----^    ^----- consumer2
  .....
 */

  def queueMultiplyProducerConsumer() = {
    val queue = new QueuePrCns()
    val consumers = (1 to 10).map(_ => new Thread(() => {
        (1 to 10).foreach(_ => {
          Thread.sleep(1500)
          println("[consumer] consume variable: " + queue.getValue)
        })
      })
    )

    val producers = (1 to 10).map(_ =>
      new Thread(() => {
      (1 to 10).foreach(_ => {
        println("[producer] Hard work started...")
        Thread.sleep(500)
        val result = Random.nextInt(42)
        println("[producer] calculated value is: " + result)
        queue.addValue(result)
      })
    }))

    consumers.foreach(_.start())
    producers.foreach(_.start())
    consumers.foreach(_.join())
    producers.foreach(_.join())
    println(queue)
  }

//  queueMultiplyProducerConsumer()

  /*
  1. Create a deadlock
  2. Create a livelock
   */

  class Deadlock(val num: Int) {
    private var state: Int = 0
    def updateState(deadlock: Option[Deadlock], newState: Int): Unit = {
      state = newState
      println(s"[deadlock #$num] state updated, state is: ${state}")
      this.synchronized {
        deadlock.foreach(d => {
          println(s"[deadlock #$num] go to next state...")
          Thread.sleep(3000)
          d.updateState(None, newState)
        })
      }
    }
  }

  object Deadlock {
    def deadlock = {
      val deadlock1 = new Deadlock(1)
      val deadlock2 = new Deadlock(2)
      val thread1 = new Thread(() => deadlock1.updateState(Some(deadlock2), 10))
      val thread2 = new Thread(() => deadlock2.updateState(Some(deadlock1), 20))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
    }
  }

//  Deadlock.deadlock
  class LiveLock(val num: Int) {
    private var state: Int = num * 10
    private var isLocked: Option[Thread] = None
    def lock(t: Thread): Boolean = {
      if (isLocked.isEmpty || isLocked.get.eq(t)) {
        isLocked = Some(t)
        true
      } else {
        false
      }
    }
    def unlock(t: Thread): Boolean = {
      if (isLocked.nonEmpty && isLocked.get.eq(t)) {
        isLocked = None
        true
      } else {
        false
      }
    }
    def update(newState: Int) = state = newState
  }

  object LiveLock {
    def liveLock() = {
      val liveLock1 = new LiveLock(1)
      val liveLock2 = new LiveLock(2)
      val thread1 = new Thread(() => {
        val t = Thread.currentThread()
        while(true) {
          println(s"[Thread #1] try lock 1....")
          if (liveLock1.lock(t)) {
            println(s"[Thread #1] locked 1....")
            Thread.sleep(1000)
            println(s"[Thread #1] try lock 2....")
            if(liveLock2.lock(t)) {
              println(s"[Thread #1] locked 2....")
              Thread.sleep(1000)
              liveLock1.update(1)
              liveLock2.update(1)
            }
            println(s"[Thread #1] try to unlock 1....")
            liveLock1.unlock(t)
            Thread.sleep(1000)
            println(s"[Thread #1] try to unlock 2....")
            liveLock2.unlock(t)
          }
        }
      })

      val thread2 = new Thread(() => {
        val t = Thread.currentThread()
        while (true) {
          println(s"[Thread #2] try lock 2....")
          if (liveLock2.lock(t)) {
            println(s"[Thread #2] locked 2....")
            Thread.sleep(1000)
            println(s"[Thread #2] try lock 1....")
            if (liveLock1.lock(t)) {
              println(s"[Thread #2] locked 1....")
              Thread.sleep(1000)
              liveLock1.update(1)
              liveLock2.update(1)
            }
            println(s"[Thread #2] try to unlock 1....")
            liveLock2.unlock(t)
            Thread.sleep(1000)
            println(s"[Thread #2] try to unlock 2....")
            liveLock1.unlock(t)
          }
        }
      })

      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
    }
  }

  LiveLock.liveLock()
}
