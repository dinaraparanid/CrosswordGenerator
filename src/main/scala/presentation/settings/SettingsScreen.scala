package presentation.settings

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme

import zio.{URIO, ZIO}

import java.awt.GridBagLayout
import javax.swing.{JLabel, JPanel}

def SettingsScreen(): URIO[AppConfig, JPanel] =
  val panel = new JPanel:
    setLayout(GridBagLayout())
    add(JLabel("Settings Screen"))

  def recompose(theme: Theme): Unit =
    panel setBackground theme.backgroundColor

  for {
    themes ← appThemeStream()
    _      ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield panel
