package com.derried.fpcourse2

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
object FuturePromises extends App {

  def longComputation = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    longComputation
  } // (global) using implicitly

  println(aFuture.value) // empty Option[Try[U]]

  aFuture.onComplete {
    case Success(value) => println(s"Future finished, result is: $value")
    case Failure(exception) => println(s"Can't finish future, because err: $exception")
  }

  println("Wait until future is complete....")
  Thread.sleep(3000)

  case class Person(id: String, name: String) {
    def poke(person: Person) =
      println(s"${name} poked ${person.name}")
  }

  val socialNetwork = Map(
    "fb.01.srv" -> Person("fb.01.srv", "Mike"),
    "fb.02.srv" -> Person("fb.02.srv", "Bill"),
    "fb.00.srv" -> Person("fb.00.srv", "Stub")
  )

  val socialNetworkFriendsList = Map(
    "fb.01.srv" -> Person("fb.02.srv", "Bill"),
    "fb.02.srv" -> Person("fb.01.srv", "Mike")
  )

  def getPersonById(id: String): Future[Option[Person]] = Future {
    Thread.sleep(200)
    socialNetwork.get(id)
  }

  def getPersonFriend(person: Person): Future[Option[Person]] = Future {
    Thread.sleep(200)
    socialNetworkFriendsList.get(person.id)
  }

  // Example of callback implementation
  def socialNetworkExecExample1: Unit = {
    val myId = "fb.01.srv"
    val i = getPersonById(myId)
    i.onComplete {
      case Failure(exception) => println("Some problem with connection...")
      case Success(meValue) => {
        meValue match {
          case Some(me) => {
            val friend = getPersonFriend(me)
            friend.onComplete {
              case Failure(e) => println(s"Some problem with connection...")
              case Success(value) => value match {
                case Some(friend) => me.poke(friend)
                case None => println(s"User's ${me.name} with id: ${myId}, doesn't exist")
              }
            }
          }
          case None => println(s"User with id: ${myId}, doesn't exist")
        }
      }
    }
  }

  socialNetworkExecExample1
  println("Wait unit find person in social network....")
  Thread.sleep(2000)

  def socialNetworkExecExample2: Unit = {
    val id = "fb.01.srv"
    getPersonById(id).foreach(x => x.foreach(mePerson =>
      getPersonFriend(mePerson)
        .foreach(res => res.foreach(friend => mePerson.poke(friend)))
    ))
  }
  socialNetworkExecExample2
  Thread.sleep(2000)
  def socialNetworkExecExample3: Unit = {
    for {
      x <- getPersonById("fb.01.srv")
      me <- x
      y <- getPersonFriend(me)
      friend <- y
    } me.poke(friend)
  }

  socialNetworkExecExample3
  Thread.sleep(2000)
}
