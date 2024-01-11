package presentation.ui.utils

import javax.swing.AbstractButton

extension (button: AbstractButton)
  def removeActionListeners(): Unit =
    button
      .getActionListeners
      .foreach(button.removeActionListener)
