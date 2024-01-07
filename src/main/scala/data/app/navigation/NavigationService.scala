package data.app.navigation

import zio.{Ref, ZLayer}

import java.awt.CardLayout
import javax.swing.JPanel

final class NavigationService(val nav: Ref[Option[Navigator]]):
  def invalidate(panel: JPanel, card: CardLayout): zio.UIO[Unit] =
    nav set Option(Navigator(panel, card))

object NavigationService:
  val layer: zio.ULayer[NavigationService] = ZLayer {
    Ref make Option.empty[Navigator] map (NavigationService(_))
  }