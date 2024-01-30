package com.derried.fpcourse2

object UsingAndExtension extends App {
  val xs = List(3,5,2,1)

  println(xs.sorted)

  object GivensInstances {
    given descIntSorting: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }

  def sorting(xs: List[Int])(using Ordering[Int]): Unit = {
    println(xs.sorted)
  }

  sorting(xs)

  object ImplicitOps {
    implicit class RichInt(val x: Int) extends AnyVal {
      def isEven: Boolean = x % 2 == 0

      def sqrt: Double = Math.sqrt(x)

      def times[T](f: () => Unit) =
        (1 to x).foreach(_ => f())

      def *[T](xs: List[T]): List[T] =
        (1 to x).flatMap(_ => xs).toList
    }
  }

  extension (x: Int) {
    def isEven: Boolean = x % 2 == 0

    def sqrt: Double = Math.sqrt(x)

    def times[T](f: () => Unit) =
      (1 to x).foreach(_ => f())

    def *[T](xs: List[T]): List[T] =
      (1 to x).flatMap(_ => xs).toList
  }

  2.times(() => {println("test")})
}
