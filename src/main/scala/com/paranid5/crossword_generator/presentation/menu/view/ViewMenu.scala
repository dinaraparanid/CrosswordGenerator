package com.paranid5.crossword_generator.presentation.menu.view

import com.paranid5.crossword_generator.data.app.AppBroadcast
import com.paranid5.crossword_generator.data.app.navigation.NavigationService
import com.paranid5.crossword_generator.presentation.ui.utils.ctrlKey
import com.paranid5.crossword_generator.presentation.{appBroadcast, navigator}

import zio.{RIO, URIO, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem}

def ViewMenu(): RIO[AppBroadcast & NavigationService, JMenu] =
  val menu = JMenu("View")

  def setContentOfMenu(appearanceItem: JMenuItem): Unit =
    menu add appearanceItem
    menu add FontMenuItem()

  for
    appearanceItem ← ThemeMenuItem()
    _              ← ZIO attempt
      setContentOfMenu(appearanceItem)
  yield menu

private def ThemeMenuItem(): URIO[AppBroadcast & NavigationService, JMenuItem] =
  for app ← appBroadcast() 
    yield new JMenuItem("Theme"):
      setAccelerator(ctrlKey(KeyEvent.VK_U))
      addActionListener: _ ⇒
        app.resetTheme()

private def FontMenuItem(): JMenuItem =
  new JMenuItem("Font"):
    addActionListener { _ ⇒ println("Font") }
