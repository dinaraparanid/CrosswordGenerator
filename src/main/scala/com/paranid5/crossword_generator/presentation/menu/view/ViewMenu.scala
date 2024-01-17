package com.paranid5.crossword_generator.presentation.menu.view

import com.paranid5.crossword_generator.data.app.AppConfigChannel
import com.paranid5.crossword_generator.data.app.navigation.NavigationService
import com.paranid5.crossword_generator.presentation.appConfigBroadcast
import com.paranid5.crossword_generator.presentation.ui.utils.ctrlKey

import zio.{RIO, ZIO, Runtime, Unsafe}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem}

/**
 * View menu to manipulate with UI preferences
 *
 * @return the whole menu with all items
 */

def ViewMenu(): RIO[AppConfigChannel & NavigationService, JMenu] =
  val menu = JMenu("View")

  @inline
  def impl(themeItem: JMenuItem): Unit =
    menu add themeItem
    menu add FontMenuItem()

  for
    themeItem ← ThemeMenuItem()
    _         ← ZIO attempt impl(themeItem)
  yield menu

/**
 * Provides functionality to switch app themes
 *
 * @return [[JMenuItem]] with the ability to switch themes
 * @see [[AppConfigChannel.resetTheme]]
 */

private def ThemeMenuItem(): RIO[AppConfigChannel & NavigationService, JMenuItem] =
  val menuItem = new JMenuItem("Theme"):
    setAccelerator(ctrlKey(KeyEvent.VK_U))

  val runtime = Runtime.default

  @inline
  def impl(app: AppConfigChannel): Unit =
    menuItem addActionListener: _ ⇒
      Unsafe.unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            app.resetTheme()

  for
    app ← appConfigBroadcast()
    _   ← ZIO attempt impl(app)
  yield menuItem

private def FontMenuItem(): JMenuItem =
  new JMenuItem("Font"):
    addActionListener { _ ⇒ println("Font") }
