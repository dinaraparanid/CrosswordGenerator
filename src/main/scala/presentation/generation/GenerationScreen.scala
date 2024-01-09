package presentation.generation

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme

import zio.{URIO, ZIO}

import java.awt.GridBagLayout
import javax.swing.{JLabel, JPanel}

def GenerationScreen(): URIO[AppConfig, JPanel] =
  val panel = new JPanel:
    setLayout(GridBagLayout())
    add(JLabel("Generation Screen"))

  def recompose(theme: Theme): Unit =
    panel setBackground theme.backgroundColor

  for {
    themes ← appThemeStream()
    _      ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield panel
