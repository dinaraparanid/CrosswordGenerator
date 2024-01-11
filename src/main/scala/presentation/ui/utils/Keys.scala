package presentation.ui.utils

import java.awt.{Toolkit, AWTEvent}
import java.awt.event.InputEvent
import javax.swing.KeyStroke

def ctrlKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, ctrl)

def ctrlShiftKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, ctrl | InputEvent.SHIFT_DOWN_MASK)

private def ctrl: Int =
  Toolkit.getDefaultToolkit.getMenuShortcutKeyMaskEx

def altKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, InputEvent.ALT_DOWN_MASK)