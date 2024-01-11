package presentation.generation

import data.app.{AppConfig, InputStates}
import presentation.generation.input.InputPanel
import presentation.ui.utils.{HorizontalSpacer, VerticalSpacer, gbc}

import zio.{RIO, ZIO}

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.JPanel

def GenerationScreen(): RIO[AppConfig & InputStates, JPanel] =
  val panel = new JPanel:
    setLayout(GridBagLayout())

  def setContentOfPanel(inputPanel: JPanel): Unit =
    panel.add(VerticalSpacer(height = 20), topSpacerGBC)
    panel.add(inputPanel, inputGBC)
    panel.add(HorizontalSpacer(width = 10), betweenSpacerGBC)
    panel.add(CrosswordSheetView(), crosswordSheetGBC)
    panel.add(VerticalSpacer(height = 40), bottomSpacerGBC)

  for {
    inputs ← InputPanel()
    _      ← ZIO attempt setContentOfPanel(inputs)
  } yield panel

private def topSpacerGBC: GridBagConstraints =
  gbc(
    gridY = 0,
    gridX = 0,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def inputGBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 1,
    weightX = 1,
    weightY = 1,
    fil = GridBagConstraints.BOTH
  )

private def betweenSpacerGBC: GridBagConstraints =
  gbc(gridX = 1, gridY = 1)

private def crosswordSheetGBC: GridBagConstraints =
  gbc(
    gridX = 2,
    gridY = 1,
    weightX = 1,
    weightY = 1,
    fil = GridBagConstraints.BOTH
  )

private def bottomSpacerGBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 2,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )