package com.derried.fpcourse2.lesson1

import scala.util.Try

object AdvanceScala extends App {
  // Syntactic sugar
  // #1 Single arg method calls
  def printName(name: String) = println(s"Hello $name")
  printName {
    123
    1
    "Alex"
  }

  val aVariable = Try {
    throw new Exception("....")
    //...
    //...
    //...
  }

  val res = List(1,2,3).map { x => x + 1 }

  // #2 Single Abstract method
  trait Active {
    def getStatus(): String
  }

  val activePerson: Active = new Active:
    override def getStatus(): String = "I'm active person"

  val activeDog: Active = () => "I'm active dog"

  val newThread = new Thread(new Runnable {
    override def run(): Unit = println("hello scala!")
  })

  val newSweetThread = new Thread(() => println("More sweet scala!"))

  // #3 Special methods with : in the end
  class Stream[A] {
    def -->:(el: A): Stream[A] = ???
  }

//  val aNewStream = 1 -->: 2 -->: new Stream[Int]

  // #4 Method naming with ``
  class Person(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  Person("Anna") `and then said` "Scala is so sweet!"

  // #5 Infix generic type
  trait Compose[A,B]
  val newComposition: Int Compose String = new Compose[Int, String] {}

  // #6 special syntax for Update
  val aArray = Array(1,2,3,4)
  aArray(2) = 10 // aArray.update(2, 10)
  println(aArray.toList)

  // #7 Getter and Setter
  class Game {
    private var score: Int = 0
    def gameScore: Int = score
    def gameScore_=(newScore: Int): Unit = score = newScore

  }

  val game = new Game()
  game.gameScore = 10
  println(game.gameScore)
}
