package org.dedkot

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object Dining {

  def apply(): Behavior[NotUsed] = Behaviors.setup { ctx =>
    val listPhilosophers = List("Abbagnano", "Babbage", "Cabral", "Darwin", "Einstein")

    val forks = listPhilosophers.zipWithIndex.map{ case (_, i) =>
      ctx.spawn(Fork(), "Fork" + (i + 1))
    }

    val philosophers = listPhilosophers.zipWithIndex.map { case (name, i) =>
      ctx.spawn(Philosopher(name, forks(i), forks((i + 1) % forks.length)), name)
    }

    philosophers.foreach(_ ! Philosopher.Thinks)

    Behaviors.empty
  }

  def main(args: Array[String]): Unit = {
    ActorSystem(Dining(), "Table")
  }

}
