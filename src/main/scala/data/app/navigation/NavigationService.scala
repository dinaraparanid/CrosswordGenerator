package data.app.navigation

import zio.stream.SubscriptionRef
import zio.{UIO, ULayer, ZLayer}

import java.awt.CardLayout
import javax.swing.{JFrame, JPanel}

final class NavigationService(val nav: SubscriptionRef[Option[Navigator]]):
  def invalidate(panel: JPanel, card: CardLayout, frame: JFrame): UIO[Unit] =
    nav set Option(Navigator(panel, card, frame))

object NavigationService:
  val layer: ULayer[NavigationService] = ZLayer:
    SubscriptionRef make Option.empty[Navigator] map (NavigationService(_))