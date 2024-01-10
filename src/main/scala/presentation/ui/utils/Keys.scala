package presentation.ui.utils

import java.awt.Toolkit
import java.awt.event.InputEvent
import javax.swing.KeyStroke

def ctrlKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(
    key,
    Toolkit
      .getDefaultToolkit
      .getMenuShortcutKeyMaskEx
  )

def altKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, InputEvent.ALT_DOWN_MASK)