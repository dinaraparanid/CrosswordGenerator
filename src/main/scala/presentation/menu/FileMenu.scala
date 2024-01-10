package presentation.menu

import presentation.ui.utils.{ctrlKey, altKey}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem, JSeparator}

def FileMenu(): JMenu =
  val newItem = NewMenuItem()
  val openItem = OpenMenuItem()
  val exitItem = ExitMenuItem()
  val separator = JSeparator()

  new JMenu("File"):
    add(newItem)
    add(openItem)
    add(separator)
    add(exitItem)

private def NewMenuItem(): JMenuItem =
  new JMenuItem("New"):
    setAccelerator(ctrlKey(KeyEvent.VK_N))
    addActionListener { _ ⇒ println("TODO: New") }

private def OpenMenuItem(): JMenuItem =
  new JMenuItem("Open"):
    setAccelerator(ctrlKey(KeyEvent.VK_O))
    addActionListener { _ ⇒ println("TODO: Open") }

private def ExitMenuItem(): JMenuItem =
  new JMenuItem("Exit"):
    setAccelerator(altKey(KeyEvent.VK_F4))
    addActionListener { _ ⇒ sys.exit() }
