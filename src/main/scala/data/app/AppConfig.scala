package data.app

import presentation.ui.{Theme, Themes, theme}

import zio.{UIO, ZLayer, ULayer}
import zio.stream.SubscriptionRef

import java.awt.Font

case class AppConfig(
  theme: SubscriptionRef[Theme],
  font: SubscriptionRef[String]
):
  def resetTheme(): UIO[Unit] =
    for {
      t ← theme.get
      _ <- theme set oppositeTheme(t.enumValue)
    } yield ()

object AppConfig:
  val layer: ULayer[AppConfig] =
    ZLayer:
      for {
        thm ← SubscriptionRef make theme(Themes.Light)
        font ← SubscriptionRef make Font.SERIF
      } yield AppConfig(thm, font)

private def oppositeTheme(t: Themes) =
  t match
    case Themes.Light ⇒ theme(Themes.Dark)
    case Themes.Dark  ⇒ theme(Themes.Light)