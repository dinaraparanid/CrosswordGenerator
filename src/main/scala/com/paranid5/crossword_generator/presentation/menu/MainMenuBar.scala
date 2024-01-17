package com.paranid5.crossword_generator.presentation.menu

import com.paranid5.crossword_generator.data.FullEnvironment
import com.paranid5.crossword_generator.presentation.menu.edit.EditMenu
import com.paranid5.crossword_generator.presentation.menu.file.FileMenu
import com.paranid5.crossword_generator.presentation.menu.help.HelpMenu
import com.paranid5.crossword_generator.presentation.menu.view.ViewMenu

import zio.{RIO, ZIO}

import javax.swing.{JMenu, JMenuBar}

/**
 * Application menu bar that consists of
 * [[FileMenu]], [[EditMenu]], [[ViewMenu]] and [[HelpMenu]]
 *
 * @return [[JMenuBar]] with all menus placed
 */

def MainMenuBar(): RIO[FullEnvironment, JMenuBar] =
  val menu = JMenuBar()

  @inline
  def impl(
    fileMenu: JMenu,
    viewMenu: JMenu
  ): Unit =
    menu add fileMenu
    menu add EditMenu()
    menu add viewMenu
    menu add HelpMenu()

  for
    viewMenu ← ViewMenu()
    fileMenu ← FileMenu()
    _        ← ZIO attempt
      impl(fileMenu, viewMenu)
  yield menu
