package presentation.ui.utils

import javax.swing.text.JTextComponent

extension (textArea: JTextComponent)
  def removeCaretListeners(): Unit =
    textArea
      .getCaretListeners
      .foreach(textArea.removeCaretListener)
