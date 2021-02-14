package org.dedkot

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

class Fork(var takenBy: String) {

  import Fork._

  private def free(): Behavior[Command] = Behaviors.receiveMessage {
    case Take(from) =>
      takenBy = from.path.name
      from ! Taken()
      busy
    case _ => Behaviors.same
  }

  private def busy(): Behavior[Command] = Behaviors.receiveMessage {
    case Take(from) =>
      from ! Busy()
      Behaviors.same
    case Put(from) =>
      if (from.path.name.equals(takenBy)) {
        takenBy = null
        free
      }
      else {
        from ! Busy()
        Behaviors.same
      }
    case _ => Behaviors.same
  }

}

object Fork {

  def apply(): Behavior[Command] = {
    new Fork(null).free
  }

  sealed trait Command
  case class Take(who: ActorRef[Answer]) extends Command
  case class Put(who: ActorRef[Answer]) extends Command

  sealed trait Answer
  case class Taken() extends Answer
  case class Busy() extends Answer

}
