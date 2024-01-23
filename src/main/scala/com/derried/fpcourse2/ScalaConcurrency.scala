package com.derried.fpcourse2

import java.util.concurrent.Executors

object ScalaConcurrency extends App {

  // JVM Threads
  val aThread = new Thread(() => println("Hello"))
  aThread.start()
  aThread.join() // Waiting until thread finish job

  val aRunnable = new Runnable {
    override def run(): Unit = println("Hello world")
  }
  aRunnable.run() // execute in the same thread
  new Thread(aRunnable) // execute in different thread

  val helloThread = new Thread(() => (1 to 5).foreach(_ => println("Hello!")))
  val goodbyeThread = new Thread(() => (1 to 5).foreach(_ => println("Goodbye!")))
  // Running in parallel
  helloThread.start()
  goodbyeThread.start()

  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => {
    Thread.sleep(1000)
    println("Done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("Mostly done after 1 second")
    Thread.sleep(1000)
    println("Done after 2 second")
  })

  pool.shutdown()
//  pool.execute(() => println("Try execute after shutdown"))
//  pool.shutdownNow() // Stop execution immediately

  def parallelProblem(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    thread1.join()
    thread2.join()
    println(x)
  }
  // X maybe 1 or 2
  parallelProblem()

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, amount: Int, name: String) = {
    bankAccount.amount -= amount
  }

  def buySafe(bankAccount: BankAccount, amount: Int, name: String) = {
    bankAccount.synchronized {
      bankAccount.amount -= amount
    }
  }

  def bankRaceCondition(): Unit = {
    (1 to 100_000).foreach(_ => {
      val bankAccount = BankAccount(50000)
      val buyIphone = new Thread(() => {buy(bankAccount, 4000, "Iphone")})
      val buyShoe = new Thread(() => {buy(bankAccount, 3000, "Shoe")})
      buyIphone.start()
      buyShoe.start()
      buyIphone.join()
      buyShoe.join()
      if (bankAccount.amount != 43000) println("HAHAHA this is race condition!")
    })
  }

  bankRaceCondition()

  // Exercise
  /*
  1. Inception thread, create 1_000_000 threads. Each thread should print Thread number.
     Overall we should see threads numbers in reverse order
      1 thread create
        2 thread create
          3 thread create ....
   */
  def inceptionThread() = {
    def createThread(threadNum: Int, threadPool: Vector[Thread]): Unit = {
      if (threadNum < 100_000) {
        val thread = new Thread(() => {
          println(s"Thread num: $threadNum")
        })
        createThread(threadNum + 1, threadPool.appended(thread))
      } else {
        threadPool.reverse.foreach(thread => {
          thread.start()
          thread.join()
        })
      }
    }
    createThread(0, Vector())
  }

  inceptionThread()
  // 2. What would be min and max X, min is 1 max is 100
  def minMaxX() = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }

  // 3. Sleep fallacy, what would be result. Result can be both: scala awesome and scala sucks
  def sleepFallacy() = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "scala is awesome!"
    })

    message = "scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    // Solution to minimize possibility that we print yearly that assign scala is awesome to message var
    awesomeThread.join()
    println(message)
  }
}
