package data.app

import presentation.ui.{Theme, Themes, theme}
import zio.ZLayer

import java.awt.Font

case class AppConfig(theme: Theme, font: String)

object AppConfig:
  val layer: zio.ULayer[AppConfig] =
    val config = AppConfig(
      theme = theme(Themes.Light),
      font = Font.SERIF
    )

    ZLayer.succeed(config)
