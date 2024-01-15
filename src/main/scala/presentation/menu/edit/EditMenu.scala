package presentation.menu.edit

import presentation.ui.utils.{ctrlKey, ctrlShiftKey}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem, JSeparator}

def EditMenu(): JMenu =
  new JMenu("Edit"):
    add(UndoMenuItem())
    add(RedoMenuItem())
    add(CutMenuItem())
    add(CopyMenuItem())
    add(PasteMenuItem())
    add(DeleteMenuItem())
    add(JSeparator())
    add(SortSubMenu())
    add(ReverseWordsMenuItem())
    add(JSeparator())
    add(GenerateMenuItem())
    add(ReverseArrangementMenuItem())

private def UndoMenuItem(): JMenuItem =
  new JMenuItem("Undo"):
    setAccelerator(ctrlKey(KeyEvent.VK_Z))
    addActionListener { _ ⇒ println("TODO: Undo") }

private def RedoMenuItem(): JMenuItem =
  new JMenuItem("Redo"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_R))
    addActionListener { _ ⇒ println("TODO: Redo") }

private def CutMenuItem(): JMenuItem =
  new JMenuItem("Cut"):
    setAccelerator(ctrlKey(KeyEvent.VK_CUT))
    addActionListener { _ ⇒ println("TODO: Cut") }

private def CopyMenuItem(): JMenuItem =
  new JMenuItem("Copy"):
    setAccelerator(ctrlKey(KeyEvent.VK_COPY))
    addActionListener { _ ⇒ println("TODO: Copy") }

private def PasteMenuItem(): JMenuItem =
  new JMenuItem("Paste"):
    setAccelerator(ctrlKey(KeyEvent.VK_PASTE))
    addActionListener { _ ⇒ println("TODO: Paste") }

private def DeleteMenuItem(): JMenuItem =
  new JMenuItem("Delete"):
    setMnemonic(KeyEvent.VK_DELETE)
    addActionListener { _ ⇒ println("TODO: Delete") }

private def SortSubMenu(): JMenu =
  new JMenu("Sort By"):
    add(SortAlphabeticMenuItem())
    add(SortLengthMenuItem())
    add(SortArrangementMenuItem())

private def SortAlphabeticMenuItem(): JMenuItem =
  new JMenuItem("Alphabetic"):
    addActionListener { _ ⇒ println("TODO: Sort alphabetic") }

private def SortLengthMenuItem(): JMenuItem =
  new JMenuItem("By Length"):
    addActionListener { _ ⇒ println("TODO: Sort by length") }

private def SortArrangementMenuItem(): JMenuItem =
  new JMenuItem("By Arrangement"):
    addActionListener { _ ⇒ println("TODO: Sort by arrangement")}

private def ReverseWordsMenuItem(): JMenuItem =
  new JMenuItem("Reverse Words"):
    addActionListener { _ ⇒ println("TODO: Reverse words") }

private def GenerateMenuItem(): JMenuItem =
  new JMenuItem("Generate"):
    setAccelerator(ctrlKey(KeyEvent.VK_G))
    addActionListener { _ ⇒ println("TODO: Generate") }

private def ReverseArrangementMenuItem(): JMenuItem =
  new JMenuItem("Reverse Arrangement"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_R))
    addActionListener { _ ⇒ println("TODO: Reverse") }