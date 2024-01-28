package com.derried.fpcourse2

object Implicit extends App {

  val aVariable1 = 1 -> 2
  val aVariable2 = "Hello" -> 2

  case class Person(name: String) {
    def greet() = println(s"Hello, my name is $name!")
  }

  Person("John").greet()

  implicit def stringToPerson(str: String): Person = Person(str)
  "Alex".greet()

  // Implicit resolving...
//  case class A() {
//    def greet() = ???
//  }
//
//  implicit def stringToA(str: String): A = A()

  def sum(x:Int)(implicit y: Int) = x + y
  implicit val y: Int = 10
  println(sum(2))

}
