package org.dedkot

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import org.dedkot.Philosopher.{Eats, ForkAnswer, State, Thinks}

import scala.util.Random

class Philosopher(ctx: ActorContext[State],
                  name: String,
                  leftFork: ActorRef[Fork.Command],
                  rightFork: ActorRef[Fork.Command]) {

  ctx.log.info("Hello, I'm {}", name)

  private def thinks(): Behavior[State] = Behaviors.receiveMessage {
    case Thinks =>
      simulateAction()

      ctx.self ! Eats
      hungry
    case _ => Behaviors.same
  }

  private def hungry(): Behavior[State] = Behaviors.receiveMessage {
    case Eats =>
      leftFork ! Fork.Take(ctx.messageAdapter(ForkAnswer))
      takeLeftFork
    case _ => Behaviors.same
  }

  private def takeLeftFork(): Behavior[State] = Behaviors.receiveMessage {
    case ForkAnswer(Fork.Taken) =>
      rightFork ! Fork.Take(ctx.messageAdapter(ForkAnswer))
      takeRightFork
    case ForkAnswer(Fork.Busy) =>
      ctx.self ! Thinks
      thinks
    case _ => Behaviors.same
  }

  private def takeRightFork(): Behavior[State] = Behaviors.receiveMessage {
    case ForkAnswer(Fork.Taken) =>
      ctx.self ! Eats
      eats
    case ForkAnswer(Fork.Busy) =>
      leftFork ! Fork.Put(ctx.messageAdapter(ForkAnswer))

      ctx.self ! Thinks
      thinks
    case _ => Behaviors.same
  }

  private def eats(): Behavior[State] = Behaviors.receiveMessage {
    case Eats =>
      ctx.log.info("{} eats use {} and {}", name, leftFork.path.name, rightFork.path.name)
      simulateAction()

      leftFork ! Fork.Put(ctx.messageAdapter(ForkAnswer))
      rightFork ! Fork.Put(ctx.messageAdapter(ForkAnswer))

      ctx.log.info("{} end eats and put {} and {}", name, leftFork.path.name, rightFork.path.name)
      ctx.self ! Thinks
      thinks
    case _ => Behaviors.same
  }

  private def simulateAction() = {
    val actionTime = 3000 + Random.nextInt(7000)
    Thread.sleep(actionTime)
  }

}

object Philosopher {

  sealed trait State
  case object Thinks extends State
  case object Eats extends State
  final case class ForkAnswer(msg: Fork.Answer) extends State

  def apply(name: String,
            leftFork: ActorRef[Fork.Command],
            rightFork: ActorRef[Fork.Command]): Behavior[State] = Behaviors.setup { ctx =>
    new Philosopher(ctx, name, leftFork, rightFork).thinks
  }

}