package com.derried.fpcourse2

import java.util.Date

object JSONSerialize extends App {
  
  case class User(name: String, age: Int)
  case class Post(title: String, text: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  // Intermediate representation
  sealed trait JSONValue {
    def serialize: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def serialize: String = "\"" + value + "\""
  }
  
  final case class JSONNumber(value: Int) extends JSONValue {
    override def serialize: String = value.toString
  }

  final case class JSONDate(value: Date) extends JSONValue {
    override def serialize: String = value.toString
  }

  final case class JSONObject[T](value: Map[String, JSONValue]) extends JSONValue {
    override def serialize: String =
      value.map((k,v) => "\"" + k + "\": " + v.serialize).mkString("{", ",", "}")
  }

  final case class JSONArray[T](value: List[JSONValue]) extends JSONValue {
    override def serialize: String =
      value.map(x => x.serialize).mkString("[", ",", "]")
  }

  // Typeclass
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  implicit class JSONOps[T](val value: T) {
    def toJson(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  implicit object UserConverter extends JSONConverter[User] {
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "title" -> JSONString(post.title),
      "text" -> JSONString(post.text),
      "createdAt" -> JSONDate(post.createdAt),
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJson,
      "posts" -> JSONArray(feed.posts.map(_.toJson))
    ))
  }

  val feed = Feed(User("Alex", 32), List(
    Post("new post", "this is my first post...", new Date()),
    Post("what about...", "Friends what do you think about problem with...", new Date()),
    Post("lol", "Did you see this", new Date())
  ))

  println(feed.toJson.serialize)
}