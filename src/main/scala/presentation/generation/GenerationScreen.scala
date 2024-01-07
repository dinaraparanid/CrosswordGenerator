package presentation.generation

import data.app.AppConfig
import presentation.appTheme
import presentation.ui.Theme
import zio.ZIO

import java.awt.GridBagLayout
import javax.swing.{JLabel, JPanel}

def GenerationScreen(): ZIO[AppConfig, Nothing, JPanel] =
  def impl(theme: Theme): JPanel =
    new JPanel:
      setBackground(theme.backgroundColor)
      setLayout(GridBagLayout())
      add(JLabel("Generation Screen"))

  for {
    theme ← appTheme()
  } yield impl(theme)
