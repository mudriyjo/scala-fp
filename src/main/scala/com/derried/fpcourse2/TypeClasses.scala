package com.derried.fpcourse2

import java.util.Date

object TypeClasses extends App {

  // Option 1
  /* Disadvantages
    - Can be only implement for OUR CLASSES
    - Can have only 1 implementation
   */
  case class User(name: String, age: Int, email: String) {
    def toHtml: String = s"<div>Hello, ${name} (${age} yo) <mailto='${email}'></div>"
  }

  // Option 2
  /* Disadvantages
    - Can be only implement for OUR CLASSES
    - Not type safe
    - Need to modify code all time for changes
   */

  def htmlRenderPM(value: Any): String = {
    value match
      case User(name, age, email) => ""
      case _ => ""
  }

  // Option 3
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>Hello, ${user.name} (${user.age} yo) <mailto='${user.email}'></div>"
  }

  /* Advantages
  - More then one implementation
  - Type safe
  - Can be done for other types
   */
  object PrivateUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>Hello, ${user.name}</div>"
  }
  val user = User("John", 20, "john@gmail.com")

  println(UserSerializer.serialize(user))
  println(PrivateUserSerializer.serialize(user))
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>today is: ${date}</div>"
  }
  
}
