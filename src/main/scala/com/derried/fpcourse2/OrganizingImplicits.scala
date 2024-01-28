package com.derried.fpcourse2

object OrganizingImplicits extends App {
  implicit def reversOrder: Ordering[Int] = Ordering.fromLessThan(_ > _)
//  implicit val normalOrder: Ordering[Int] = Ordering.fromLessThan(_ > _)

  println(List(1,2,3,4,5).sorted)

  /* Implicits as parameter can be:
    - var/val
    - object
    - accessor method: def name: (without parenthesis)
   */

  case class Person(name: String, age: Int)

  val people = List(
    Person("John", 30),
    Person("Anna", 20),
    Person("Lily", 25),
  )

  implicit val personSort: Ordering[Person] = Ordering.fromLessThan((x,y) => x.name.compareTo(y.name) < 0)
  println(people.sorted)
  /* Implicits scope
    - normal scope = LOCAL
    - imported scope
    - companion object of all types involved in the method signature
   */

  /* Exercise
    - totalPrice = most used (50%)
    - by unit count = 25%
    - by unit price = 25%
   */
  case class Purchase(nUnit: Int, price: Double)
  object Purchase {
    implicit val totalPriceOrderingAsc: Ordering[Purchase] = Ordering.fromLessThan((x,y) => (x.nUnit * x.price) < (y.nUnit * y.price))
    object UnitCountOrdering {
      implicit val unitCountOrderingAsc: Ordering[Purchase] = Ordering.fromLessThan((x, y) => x.nUnit < y.nUnit)
    }

    object PriceCountOrdering {
      implicit val priceCountOrderingAsc: Ordering[Purchase] = Ordering.fromLessThan((x, y) => x.price < y.price)
    }
  }

  val xs = List(
    Purchase(10, 2.2),
    Purchase(1, 10),
    Purchase(5, 5),
  )

//  import Purchase.PriceCountOrdering._
  import Purchase.UnitCountOrdering._
  println(xs.sorted)

}
