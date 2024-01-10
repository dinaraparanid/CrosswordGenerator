package data.app

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*

import zio.stream.SubscriptionRef
import zio.{ULayer, ZLayer}

import java.awt.Font
import javax.swing.{JFrame, SwingUtilities}

case class AppConfig(
  private var theme: String,
  font: SubscriptionRef[String]
):
  def resetTheme(frame: JFrame): Unit =
    theme = theme match
      case FlatMaterialPalenightIJTheme.NAME ⇒
        FlatNightOwlIJTheme.setup()
        FlatNightOwlIJTheme.NAME

      case FlatNightOwlIJTheme.NAME ⇒
        FlatMaterialPalenightIJTheme.setup()
        FlatMaterialPalenightIJTheme.NAME

    SwingUtilities.updateComponentTreeUI(frame)

object AppConfig:
  val layer: ULayer[AppConfig] =
    ZLayer:
      for font ← SubscriptionRef make Font.SERIF
        yield AppConfig(FlatMaterialPalenightIJTheme.NAME, font)
