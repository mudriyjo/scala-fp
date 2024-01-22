package com.derried.fpcourse2.lesson1

import com.derried.fpcourse2.lesson1.AdvancePatternMatching.{BigNumber, EvenNumber, SingleDigit}

object AdvancePatternMatching extends App {

  val xs = List(1)
  xs match {
    case head :: Nil => println(s"List with only one element: $head")
    case _ =>
  }

  class Person(val name: String, val age: Int)
  object Person {
    def unapply(p: Person): Option[(String, Int)] =
      if (p.age < 21) None
      else Some((p.name, p.age))

    def unapply(x: Int): Option[(String)] =
      Some(if (x < 21) "minor" else "major")
  }

  val person = new Person("John", 25)
  person match {
    case Person(name, age) => println(s"Person name is: $name and $age years old")
    case _ => println("Some error")
  }

  person.age match {
    case Person(status) => println(s"Legal status is: $status")
  }

  object SingleDigit {
    def unapply(x: Int): Option[String] = {
      if (x < 10) then Some("single digit")
      else None
    }
  }
  object EvenNumber {
    def unapply(x: Int): Option[String] = {
      if (x % 2 == 0) Some("an even number")
      else None
    }
  }
  object BigNumber {
    def unapply(x: Int): Option[String] = {
        if (x > 10_000) Some("a big number")
        else None
    }
  }

  // Exercise
  val n: Int = 45
  val mathProperty = n match {
    case SingleDigit(msg) => println(msg)
    case EvenNumber(msg) => println(msg)
    case BigNumber(msg) => println(msg)
    case _ => println("no property")
  }

  // Infix pattern
  val xsList = List(1,2,3,4)
  val msg = xsList match {
    case List(1, 2, _*) => "Start with one and two"
    case Nil => "Empty list"
  }

  abstract class MyList[+A] {
    def isEmpty: Boolean
    def getHead: A
    def getTail: MyList[A]
    def map[B](f: A => B): MyList[B]
  }
  case object MyEmpty extends MyList[Nothing] {
    override def isEmpty: Boolean = true

    override def getHead: Nothing = throw Exception("Can't get head from empty list")

    override def getTail: MyList[Nothing] = MyEmpty

    override def map[B](f: Nothing => B): MyList[B] = MyEmpty
  }
  case class MyCons[+A](val head: A, val tail: MyList[A]) extends MyList[A] {
    override def isEmpty: Boolean = false
    override def getHead: A = head

    override def getTail: MyList[A] = tail

    override def map[B](f: A => B): MyList[B] = MyCons(f(head), tail.map(f))
  }

  object MyList {
    def unapplySeq[A](xs: MyList[A]): Option[Seq[A]] =
      if (xs == MyEmpty) Some(Seq.empty)
      else unapplySeq(xs.getTail).map(xs.getHead +: _)
  }

  val myList = MyCons(1, MyCons(2, MyCons(3, MyEmpty)))
  myList match
    case MyList(1, 2, _*) => println("My list start from 1 and 2")
    case _ => println("guard...")

  abstract class PersonWrapper[T] {
    def get: T
    def isEmpty: Boolean
  }

  object PersonWrapper {
    def unapply(person: Person): PersonWrapper[String] = new PersonWrapper[String] {
      def get = person.name
      def isEmpty = false
    }
  }
  val text = person match
    case PersonWrapper(name) => s"Person's name is $name"

  println(text)
}
