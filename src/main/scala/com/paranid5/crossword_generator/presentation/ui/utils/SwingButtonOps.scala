package com.paranid5.crossword_generator.presentation.ui.utils

import javax.swing.AbstractButton

extension (button: AbstractButton)
  /**
   * Removes all applied click
   * listeners from the button
   */

  def removeActionListeners(): Unit =
    button
      .getActionListeners
      .foreach(button.removeActionListener)
