package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.GridBagConstraints

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
