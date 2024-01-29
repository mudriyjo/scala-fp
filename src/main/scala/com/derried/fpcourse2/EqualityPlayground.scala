package com.derried.fpcourse2

class EqualityPlayground {
  /* Exercise
      Equal
     */
  trait Equal[T] {
    def isEqual(x: T, y: T): Boolean
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equal: Equal[T]) = equal.isEqual(a, b)
  }

  object PrivateUserEqual extends Equal[User] {
    override def isEqual(x: User, y: User): Boolean = x.name == y.name
  }

  implicit object AuthUserEqual extends Equal[User] {
    override def isEqual(x: User, y: User): Boolean = x.name == y.name && x.email == y.email
  }

  val user1 = User("John", 20, "john@gmail.com")
  val user2 = User("John", 20, "john@rockthejvm.com")

  println(s"the user is equal: ${Equal(user1, user2)}")
}
