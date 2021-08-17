package org.dedkot

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

class Fork(private var takenBy: Option[String]) {

  import Fork._

  private def free(): Behavior[Action] = Behaviors.receiveMessage {
    case Take(from) =>
      takenBy = Option(from.path.name)
      from ! Taken
      busy
    case _ => Behaviors.same
  }

  private def busy(): Behavior[Action] = Behaviors.receiveMessage {
    case Take(from) =>
      from ! Busy
      Behaviors.same
    case Put(from) =>
      if (takenBy.getOrElse(false).equals(from.path.name)) {
        takenBy = None
        free
      }
      else {
        from ! Busy
        Behaviors.same
      }
    case _ => Behaviors.same
  }

  def getTakenBy = takenBy

}

object Fork {

  def apply(): Behavior[Action] = {
    new Fork(None).free
  }

  sealed trait Action
  case class Take(from: ActorRef[Answer]) extends Action
  case class Put(from: ActorRef[Answer]) extends Action

  sealed trait Answer
  case object Taken extends Answer
  case object Busy extends Answer

}
