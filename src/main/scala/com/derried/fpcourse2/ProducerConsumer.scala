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

  queueProducerConsumer()
}
