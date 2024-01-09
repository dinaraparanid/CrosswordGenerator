package data.app.navigation

import zio.stream.SubscriptionRef
import zio.{ULayer, ZLayer, UIO}

import java.awt.CardLayout
import javax.swing.JPanel

final class NavigationService(val nav: SubscriptionRef[Option[Navigator]]):
  def invalidate(panel: JPanel, card: CardLayout): UIO[Unit] =
    nav set Option(Navigator(panel, card))

object NavigationService:
  val layer: ULayer[NavigationService] = ZLayer {
    SubscriptionRef make Option.empty[Navigator] map (NavigationService(_))
  }