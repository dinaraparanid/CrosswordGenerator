package presentation.menu

import presentation.ui.utils.{ctrlKey, ctrlShiftKey}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem, JSeparator}

def FileMenu(): JMenu =
  new JMenu("File"):
    add(NewMenuItem())
    add(OpenMenuItem())
    add(JSeparator())
    add(SaveMenuItem())
    add(SaveAsMenuItem())
    add(ExportMenuItem())
    add(JSeparator())
    add(SettingsMenuItem())
    add(JSeparator())
    add(ExitMenuItem())

private def NewMenuItem(): JMenuItem =
  new JMenuItem("New"):
    setAccelerator(ctrlKey(KeyEvent.VK_N))
    addActionListener { _ ⇒ println("TODO: New") }

private def OpenMenuItem(): JMenuItem =
  new JMenuItem("Open"):
    setAccelerator(ctrlKey(KeyEvent.VK_O))
    addActionListener { _ ⇒ println("TODO: Open") }

private def SaveMenuItem(): JMenuItem =
  new JMenuItem("Save"):
    setAccelerator(ctrlKey(KeyEvent.VK_S))
    addActionListener { _ ⇒ println("TODO: Save") }

private def SaveAsMenuItem(): JMenuItem =
  new JMenuItem("Save As"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_S))
    addActionListener { _ ⇒ println("TODO: Save As") }

private def ExportMenuItem(): JMenuItem =
  new JMenuItem("Export"):
    setAccelerator(ctrlKey(KeyEvent.VK_E))
    addActionListener { _ ⇒ println("TODO: Export") }

private def SettingsMenuItem(): JMenuItem =
  new JMenuItem("Settings"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_G))
    addActionListener { _ ⇒ println("TODO: Settings") }

private def ExitMenuItem(): JMenuItem =
  new JMenuItem("Quit"):
    setAccelerator(ctrlKey(KeyEvent.VK_Q))
    addActionListener { _ ⇒ sys.exit() }
