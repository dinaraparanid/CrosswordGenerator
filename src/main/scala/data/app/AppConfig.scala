package data.app

import presentation.ui.{Theme, Themes, theme}

import zio.{UIO, ZLayer, ULayer}
import zio.stream.SubscriptionRef

import java.awt.Font

case class AppConfig(
  theme: SubscriptionRef[Theme],
  font: SubscriptionRef[String]
)

object AppConfig:
  val layer: ULayer[AppConfig] =
    ZLayer:
      for {
        thm  ← SubscriptionRef make theme(Themes.Light)
        font ← SubscriptionRef make Font.SERIF
      } yield AppConfig(thm, font)

extension (config: AppConfig)
  def resetTheme(): UIO[Unit] =
    for {
      t ← config.theme.get
      _ ← config.theme set oppositeTheme(t.enumValue)
    } yield ()

private def oppositeTheme(t: Themes) =
  t match
    case Themes.Light ⇒ theme(Themes.Dark)
    case Themes.Dark  ⇒ theme(Themes.Light)