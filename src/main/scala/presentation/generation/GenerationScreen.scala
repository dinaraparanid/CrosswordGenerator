package presentation.generation

import data.app.{AppConfig, InputStates}
import presentation.appThemeStream
import presentation.generation.input.InputPanel
import presentation.ui.Theme
import zio.{RIO, ZIO}

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.JPanel

def GenerationScreen(): RIO[AppConfig & InputStates, JPanel] =
  val panel = new JPanel:
    setLayout(GridBagLayout())

  def setContentOfPanel(inputPanel: JPanel): Unit =
    panel.add(inputPanel, initialConstraints)

  def recompose(theme: Theme): Unit =
    panel setBackground theme.backgroundColor

  for {
    themes ← appThemeStream()
    inputs ← InputPanel()

    _ ← ZIO attempt setContentOfPanel(inputs)
    _ ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield panel

private def initialConstraints: GridBagConstraints =
  new GridBagConstraints:
    anchor = GridBagConstraints.FIRST_LINE_START
    fill = GridBagConstraints.BOTH
    /*gridx = 0
    gridy = 0*/