package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.GridBagConstraints

/**
 * Simplified [[GridBagConstraints]] constructor
 *
 * @param gridX      start column index of the cell to put component into
 * @param gridY      start row index of the cell to put component into
 * @param gridWidth  number of cells that component will occupy horizontally
 * @param gridHeight number of cell that component will occupy vertically
 * @param weightX    horizontal weight of the component (occupy free space ratio)
 * @param weightY    vertical weight of the component (occupy free space ratio)
 * @param anc        anchor with which component will be connected
 * @param fil        what directions should component fill
 * @return [[GridBagConstraints]] with applied parameters
 */

def gbc(
  gridX: Int = GridBagConstraints.RELATIVE,
  gridY: Int = GridBagConstraints.RELATIVE,
  gridWidth: Int = 1,
  gridHeight: Int = 1,
  weightX: Int = 0,
  weightY: Int = 0,
  anc: Int = GridBagConstraints.CENTER,
  fil: Int = GridBagConstraints.NONE
) = new GridBagConstraints:
  gridx = gridX
  gridy = gridY
  gridwidth = gridWidth
  gridheight = gridHeight
  weightx = weightX
  weighty = weightY
  anchor = anc
  fill = fil
