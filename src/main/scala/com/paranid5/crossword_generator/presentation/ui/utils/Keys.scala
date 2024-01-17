package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.Toolkit
import java.awt.event.InputEvent
import javax.swing.KeyStroke

/** Ctrl + ? event */

def ctrlKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, ctrl)

/** Ctrl + Shift + ? event */

def ctrlShiftKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, ctrl | InputEvent.SHIFT_DOWN_MASK)

/** Ctrl event */

private def ctrl: Int =
  Toolkit.getDefaultToolkit.getMenuShortcutKeyMaskEx

/** Alt + ? event */

def altKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, InputEvent.ALT_DOWN_MASK)