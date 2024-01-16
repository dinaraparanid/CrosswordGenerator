package com.paranid5.crossword_generator.presentation.generation

import com.paranid5.crossword_generator.data.app.{AppBroadcast, SessionBroadcast}
import com.paranid5.crossword_generator.data.storage.StoragePreferences
import com.paranid5.crossword_generator.presentation.generation.input.InputPanel
import com.paranid5.crossword_generator.presentation.ui.utils.{HorizontalSpacer, VerticalSpacer, gbc}
import zio.{RIO, Scope, ZIO}

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.{JPanel, JSplitPane}

def GenerationScreen(): RIO[StoragePreferences & SessionBroadcast & Scope, JPanel] =
  val panel = new JPanel:
    setLayout(GridBagLayout())

  def setContentOfPanel(
    inputPanel:    JPanel,
    crosswordView: JSplitPane
  ): Unit =
    panel.add(VerticalSpacer(height = 20), topSpacerGBC)
    panel.add(inputPanel, inputGBC)
    panel.add(HorizontalSpacer(width = 10), betweenSpacerGBC)
    panel.add(crosswordView, crosswordSheetGBC)
    panel.add(VerticalSpacer(height = 40), bottomSpacerGBC)

  for
    inputs    ← InputPanel()
    crossword ← CrosswordSheetView()
    _         ← ZIO attempt setContentOfPanel(inputs, crossword)
  yield panel

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