package com.derried.fpcourse2

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}
import scala.concurrent.duration.*

object FuturePromises extends App {

//  def longComputation = {
//    Thread.sleep(2000)
//    42
//  }
//
//  val aFuture = Future {
//    longComputation
//  } // (global) using implicitly
//
//  println(aFuture.value) // empty Option[Try[U]]
//
//  aFuture.onComplete {
//    case Success(value) => println(s"Future finished, result is: $value")
//    case Failure(exception) => println(s"Can't finish future, because err: $exception")
//  }
//
//  println("Wait until future is complete....")
//  Thread.sleep(3000)
//
//  case class Person(id: String, name: String) {
//    def poke(person: Person) =
//      println(s"${name} poked ${person.name}")
//  }
//
//  val socialNetwork = Map(
//    "fb.01.srv" -> Person("fb.01.srv", "Mike"),
//    "fb.02.srv" -> Person("fb.02.srv", "Bill"),
//    "fb.00.srv" -> Person("fb.00.srv", "Stub")
//  )
//
//  val socialNetworkFriendsList = Map(
//    "fb.01.srv" -> Person("fb.02.srv", "Bill"),
//    "fb.02.srv" -> Person("fb.01.srv", "Mike")
//  )
//
//  def getPersonById(id: String): Future[Option[Person]] = Future {
//    Thread.sleep(200)
//    socialNetwork.get(id)
//  }
//
//  def getPersonFriend(person: Person): Future[Option[Person]] = Future {
//    Thread.sleep(200)
//    socialNetworkFriendsList.get(person.id)
//  }
//
//  // Example of callback implementation
//  def socialNetworkExecExample1: Unit = {
//    val myId = "fb.01.srv"
//    val i = getPersonById(myId)
//    i.onComplete {
//      case Failure(exception) => println("Some problem with connection...")
//      case Success(meValue) => {
//        meValue match {
//          case Some(me) => {
//            val friend = getPersonFriend(me)
//            friend.onComplete {
//              case Failure(e) => println(s"Some problem with connection...")
//              case Success(value) => value match {
//                case Some(friend) => me.poke(friend)
//                case None => println(s"User's ${me.name} with id: ${myId}, doesn't exist")
//              }
//            }
//          }
//          case None => println(s"User with id: ${myId}, doesn't exist")
//        }
//      }
//    }
//  }
//
//  socialNetworkExecExample1
//  println("Wait unit find person in social network....")
//  Thread.sleep(2000)
//
//  def socialNetworkExecExample2: Unit = {
//    val id = "fb.01.srv"
//    getPersonById(id).foreach(x => x.foreach(mePerson =>
//      getPersonFriend(mePerson)
//        .foreach(res => res.foreach(friend => mePerson.poke(friend)))
//    ))
//  }
//  socialNetworkExecExample2
//  Thread.sleep(2000)
//  def socialNetworkExecExample3: Unit = {
//    for {
//      x <- getPersonById("fb.01.srv")
//      me <- x
//      y <- getPersonFriend(me)
//      friend <- y
//    } me.poke(friend)
//  }
//
//  socialNetworkExecExample3
//  Thread.sleep(2000)
//
//  // SimpleBanking app
//  case class User(name: String)
//  case class Transaction(from: String, to: String, amount: Double, status: String)
//
//  object BankingApp {
//    val name = "Rock the JVM app"
//
//    def fetchUser(name: String): Future[User] = Future {
//      // Simulate computation
//      Thread.sleep(200)
//      User(name)
//    }
//
//    def createTransaction(from: User, to: String, amount: Double): Future[Transaction] = Future {
//      // Simulate processes...
//      Thread.sleep(500)
//      Transaction(from.name, to, amount, "SUCCESS")
//    }
//
//    def makePurchase(userName: String, merchant: String, item: String, amount: Double): String = {
//      val transactionStatusFuture = for {
//        user <- fetchUser(userName)
//        transaction <- createTransaction(user, merchant, amount)
//      } yield transaction.status
//
//      Await.result(transactionStatusFuture, 2.seconds)
//    }
//  }
//
//  println(BankingApp.makePurchase("Alex", "Rock the JVM shop", "Iphone 12", 3000))
//
//  // Producer Consumer example through Promises
//  val promise = Promise[Int]()
//  val future = promise.future
//
//  future.onComplete{
//    case Success(result) => println(s"[consumer] Get result: $result")
//  }
//
//  val newThread = new Thread(() => {
//    println("[producer] crunch numbers...")
//    Thread.sleep(500)
//    println("[producer] send result")
//    promise.success(42)
//    ()
//  })
//
//  newThread.start()

  /*
  Exercises
  1) fulfill a future IMMEDIATELY return a value
  2) inSequence(fa, fb) => first calculate fa then calculate fb
  3) first(fa, fb) => get first finished future
  4) last(fa, fb) => get last finished future
  5) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // Exercise 1
  def immediateFuture[T](el: T) = Future(el)
  // Exercise 2
  def createFuture(name: String, timeout: Int) = {
    Future {
      Thread.sleep(Random.nextInt(timeout))
      val res = Random.nextInt(10)
      println(s"Future `$name` is ready, result: $res")
      res
    }
  }

  def inSequence(fa: => Future[Int], fb: => Future[Int]) = {
    val res = fa.flatMap(x => fb.map(y => (x,y)))
    println(s"Sequence: ${Await.result(res, 2.seconds)}")
  }
  inSequence(createFuture("A", 1000), createFuture("B", 10))
  // Exercise 3
  def first(fa: Future[Int], fb: Future[Int]) = {
    val res = Future {
      fa
      fb
    }
    println(s"First : ${Await.result(res, 2.seconds)}")
  }
  first(createFuture("A1", 1000), createFuture("B1", 10))

  def first_2(fa: Future[Int], fb: Future[Int]) = {
    val res = Future.firstCompletedOf(List(fa,fb))
    println(s"First : ${Await.result(res, 2.seconds)}")
  }

  first_2(createFuture("A2", 1000), createFuture("B2", 10))
  // Exercise 4
  def last(fa: Future[Int], fb: Future[Int]) = {
    val result = ArrayBuffer[Int]()
    fa.onComplete{
      case Success(value) => result.synchronized(result.addOne(value))
    }
    fb.onComplete {
      case Success(value) => result.synchronized(result.addOne(value))
    }
    Await.result(Future.sequence(List(fa,fb)), 2.seconds)
    println(s"Last : ${result.last}")
  }

  last(createFuture("A3", 1000), createFuture("B3", 10))
  // Exercise 5
  def retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] = {
    // Best practices
    action()
      .filter(condition)
      .recoverWith{
        case _ => retryUntil(action, condition)
      }
//    val f = action()
//    f.flatMap(res => {
//      if (condition(res)) {
//        f
//      } else {
//        retryUntil(action, condition)
//      }
//    })
  }
  Await.result(retryUntil(() => createFuture("INFINITY", 100), x => x == 5), 5.seconds)
}
