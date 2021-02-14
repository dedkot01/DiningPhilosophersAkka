package org.dedkot

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object Table {

  def apply(): Behavior[NotUsed] = Behaviors.setup { ctx =>
    val listPersons = List("Naruto", "Sasuke", "Sakura", "Kakashi", "Hinata")

    val forks = for (i <- 1 to listPersons.length) yield ctx.spawn(Fork(), "Fork" + i)
    val persons = for (
      (name, i) <- listPersons.zipWithIndex
    ) yield ctx.spawn(Person(name, forks(i), forks((i + 1) % forks.length)), name)

    persons.foreach(_ ! Person.Thinks())

    Behaviors.empty
  }

  def main(args: Array[String]): Unit = {
    ActorSystem(Table(), "Table")
  }

}
