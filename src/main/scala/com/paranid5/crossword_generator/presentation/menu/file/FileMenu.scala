package com.paranid5.crossword_generator.presentation.menu.file

import com.paranid5.crossword_generator.data.app.SessionChannel
import com.paranid5.crossword_generator.data.storage.StoragePreferences
import com.paranid5.crossword_generator.presentation.ui.utils.{ctrlKey, ctrlShiftKey}

import zio.{RIO, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem, JSeparator}

/**
 * File menu to manipulate with
 * session configs and the document
 *
 * @return the whole menu with all items
 */

def FileMenu(): RIO[StoragePreferences & SessionChannel, JMenu] =
  val menu = JMenu("File")

  @inline
  def setContent(exportMenuItem: JMenuItem): Unit =
    menu add NewMenuItem()
    menu add OpenMenuItem()
    menu add JSeparator()
    menu add SaveMenuItem()
    menu add exportMenuItem
    menu add JSeparator()
    menu add SettingsMenuItem()
    menu add JSeparator()
    menu add ExitMenuItem()

  for
    exportMenuItem ← ExportMenuItem()
    _              ← ZIO attempt
      setContent(exportMenuItem)
  yield menu

private def NewMenuItem(): JMenuItem =
  new JMenuItem("New"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_N))
    addActionListener { _ ⇒ println("TODO: New") }

private def OpenMenuItem(): JMenuItem =
  new JMenuItem("Open"):
    setAccelerator(ctrlKey(KeyEvent.VK_O))
    addActionListener { _ ⇒ println("TODO: Open") }

private def SaveMenuItem(): JMenuItem =
  new JMenuItem("Save"):
    setAccelerator(ctrlKey(KeyEvent.VK_S))
    addActionListener { _ ⇒ println("TODO: Save") }

private def SettingsMenuItem(): JMenuItem =
  new JMenuItem("Settings"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_G))
    addActionListener { _ ⇒ println("TODO: Settings") }

private def ExitMenuItem(): JMenuItem =
  new JMenuItem("Quit"):
    setAccelerator(ctrlKey(KeyEvent.VK_Q))
    addActionListener { _ ⇒ sys.exit() }
