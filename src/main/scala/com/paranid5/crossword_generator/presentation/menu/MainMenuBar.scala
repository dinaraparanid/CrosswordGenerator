package com.paranid5.crossword_generator.presentation.menu

import com.paranid5.crossword_generator.data.FullEnvironment
import com.paranid5.crossword_generator.data.app.{AppBroadcast, SessionBroadcast}
import com.paranid5.crossword_generator.data.app.navigation.NavigationService
import com.paranid5.crossword_generator.data.storage.StoragePreferences

import com.paranid5.crossword_generator.presentation.menu.edit.EditMenu
import com.paranid5.crossword_generator.presentation.menu.file.FileMenu
import com.paranid5.crossword_generator.presentation.menu.help.HelpMenu
import com.paranid5.crossword_generator.presentation.menu.view.ViewMenu

import zio.{RIO, Scope, ZIO}

import javax.swing.{JMenu, JMenuBar}

def MainMenuBar(): RIO[FullEnvironment & Scope, JMenuBar] =
  val menu = JMenuBar()

  def setContentOfMenu(
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
      setContentOfMenu(fileMenu, viewMenu)
  yield menu
