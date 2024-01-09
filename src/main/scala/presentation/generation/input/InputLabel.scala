package presentation.generation.input

import data.app.AppConfig
import presentation.{appFontStream, appThemeStream}
import presentation.ui.Theme
import presentation.ui.utils.combine

import zio.{RIO, ZIO}

import java.awt.Font
import javax.swing.JLabel

private def InputLabel(text: String): RIO[AppConfig, JLabel] =
  val label = JLabel(text)

  def recompose(theme: Theme, typeface: String): Unit =
    label setForeground theme.fontColor
    label setFont labelFont(typeface)

  for {
    themes ← appThemeStream()
    fonts  ← appFontStream()

    _ ← combine(themes, fonts).foreach { case (t, f) ⇒
      ZIO attempt recompose(t, f)
    }.fork
  } yield label

private def labelFont(typeface: String) =
  Font(typeface, Font.PLAIN, 16)
