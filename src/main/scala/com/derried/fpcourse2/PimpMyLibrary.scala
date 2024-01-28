package com.derried.fpcourse2

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(val x: Int) extends AnyVal {
    def isEven: Boolean = x % 2 == 0

    def sqrt: Double = Math.sqrt(x)
    def times[T](f: () => Unit) =
      (1 to x).foreach(_ => f())

    def *[T](xs: List[T]): List[T] =
      (1 to x).flatMap(_ => xs).toList
  }

  // pimping or type enrichment
  println(2.isEven) // new RichInt(2).isEven
  println(4.sqrt)

// Not work multiply time
//  implicit class RicherInt(val richerInt: RichInt) extends AnyVal {
//    def isOdd: Boolean = richerInt % 2 != 0
//  }
//
//  println(2.isOdd)

  /* Exercise
    Enrich the String class
    - asInt
    - encrypt
      "John" -> Lqjp

    Keep enrich the Int class
    - times(function) 3.times(() =>)
    - *, 3 * List(1,2) -> List(1,2,1,2,1,2)
   */
  3.times(() => println("Hello"))
  println(3 * List(1,2)) // -> List(1,2,1,2,1,2)

  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = value.toInt
    def encrypt(rotate: Int): String =
      val codeTable = ('a' to 'z').zip(1 to 26).toMap
      value.map(ch => {
        val normalizedChar = ch.toLower
        val newChar = (codeTable(normalizedChar) + rotate) % 26
        val res = codeTable.filter((char, code) => code == newChar).head._1
        if (ch.isLower) then res else res.toTitleCase
      })

  }

  val x: Int = "42".asInt
  println("John".encrypt(2))
}
