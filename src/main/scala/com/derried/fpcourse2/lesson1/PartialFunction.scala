package com.derried.fpcourse2.lesson1

object PartialFunction extends App {

  val function = (x: Int) => x + 1

  val aFussyFunction = (x:Int) => x match {
    case 1 => 10
    case 2 => 20
    case 3 => 30
    case _ => throw new PartialFunctionApplicationException
  }
  class PartialFunctionApplicationException extends RuntimeException

  val partialFunction: PartialFunction[Int, Int] = {
    case 1 => 10
    case 2 => 20
    case 3 => 30
  }

  println(partialFunction(1))
  println(partialFunction(3))
  println(partialFunction.lift(2))
  println(partialFunction.orElse{
    case 4 => 100
  }(4))

  val res = List(1,2,3).map(partialFunction)
  println(res)

  /*
  Partial function can have only one parameter
   */

  // Exercise
  // 1. Construct PF from anonymous class
  // 2. Create a dumb chat bot

  val testPF = new PartialFunction[Int, String] {
    private def logic(x: Int): Option[String] =
      val res = x match {
        case 1 => "one"
        case 2 => "two"
        case 3 => "three"
        case 4 => "four"
        case 5 => "five"
        case 6 => "six"
        case 7 => "seven"
        case 8 => "eight"
        case 9 => "nine"
        case _ => ""
      }
      if res.isEmpty then None else Some(res)

    override def isDefinedAt(x: Int): Boolean = logic(x).nonEmpty

    override def apply(v1: Int): String = logic(v1).get
  }

  println(testPF(1))
  println(testPF(5))
  println(testPF.isDefinedAt(15))
  println(testPF.lift(15)) // for security

  val dumbChatBot: PartialFunction[Int, String] = {
    case 1 => "List of news:..."
    case 2 => "Available songs:...."
    case 3 => "Movies:...."
    case 4 => "Weather:...."
    case 5 => "Live show:...."
    case 0 => "Menu:...."
  }

  println("menu from 1 to 5 and 0")
  scala.io.Source.stdin.getLines().foreach(line => println(dumbChatBot(line.toInt)))
//  println(dumbChatBot(1))
//  println(dumbChatBot(5))
//  println(dumbChatBot(0))
}
