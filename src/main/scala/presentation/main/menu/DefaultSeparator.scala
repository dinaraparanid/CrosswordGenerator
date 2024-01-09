package presentation.main.menu

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme

import zio.{URIO, ZIO}

import javax.swing.{JSeparator, SwingConstants}

def DefaultSeparator(): URIO[AppConfig, JSeparator] =
  val sep = new JSeparator(SwingConstants.HORIZONTAL)

  def recompose(theme: Theme): Unit =
    sep setBackground theme.backgroundAlternativeColor

  for {
    themes ← appThemeStream()
    _      ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield sep
