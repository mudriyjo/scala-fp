package com.derried.fpcourse1

object ValueVariablesTypes extends App{
    // Vals are immutable
    val x: Int = 42
    println(x)

    val aString: String = "Hello, world!"
    val anotherString = "test"

    val aBoolean: Boolean = true
    val aChar: Char = 'a'
    val aInt: Int = 4
    val aShort: Short = 1
    val aLong: Long = 1L
    val aFloat: Float = 1.0f
    val aDouble: Double = 1.0d

    //Variables
    var aVariable: Int = 5

    // sideEffects
    aVariable = 3
}
