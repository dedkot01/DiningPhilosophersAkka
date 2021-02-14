package org.dedkot

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, ScalaTestWithActorTestKit}
import org.dedkot.Fork.{Answer, Busy, Command, Put, Take, Taken}
import org.scalatest.wordspec.AnyWordSpecLike

class ForkSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  lazy val fork = testKit.spawn(Fork())
  lazy val sender1 = testKit.createTestProbe[Answer]()
  lazy val sender2 = testKit.createTestProbe[Answer]()

  "A Fork" must {
    "reply to taken" in {
      fork ! Take(sender1.ref)
      sender1.expectMessage(Taken())
    }
    "reply to busy" in {
      fork ! Take(sender2.ref)
      sender2.expectMessage(Busy())
    }
    "stay busy" in {
      fork ! Put(sender2.ref)
      sender2.expectMessage(Busy())
    }
  }

}