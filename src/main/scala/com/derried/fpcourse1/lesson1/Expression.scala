package com.derried.fpcourse1.lesson1

object Expression extends App {

  // Expressions
  val x = 1
  println(x)

  println(x + 2 * 4 ^ 2)
  // * / - + >> >>> << <<< & | ^

  println(1 == x)
  // > < == != >= <=

  println(!(1 == x))
  // ! && ||

  var xVariable = 1
  xVariable += 2
  println(xVariable)
  // += /= *= -= .... side effects

  // Instructions (Do this, do that) vs Expressions (Value)

  //IF expression
  val aCondition = true
  val aConditionValue = if(aCondition) 5 else 3
  println(aConditionValue)
  println(if(aCondition) 5 else 3)

  var i = 0
  while (i <= 10) {
    println(i)
    i += 1
  }

  val aWieredValue = (xVariable = 1) // Unit
  println(aWieredValue)

  // side effects: println(), while, reassigning ...

  // Code block

  var aCodeBlock = {
    val y = 2
    val z = y + 1
    if (z > 2) "hello" else "goodbye"
  }
  println(aCodeBlock)

//  val anotherValue = z // not visible outside

  // Boolean
  val someValue = {
    2 < 3
  }

  // String
  val otherValue = {
    if (2 > 3) 923 else 123
    "42"
  }
}
