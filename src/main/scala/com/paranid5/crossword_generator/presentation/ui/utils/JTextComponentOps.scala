package com.paranid5.crossword_generator.presentation.ui.utils

import javax.swing.text.JTextComponent

extension (component: JTextComponent)
  /** Removes all caret listeners */

  def removeCaretListeners(): Unit =
    component
      .getCaretListeners
      .foreach(component.removeCaretListener)
