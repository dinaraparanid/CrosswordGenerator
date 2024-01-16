package presentation.menu

import data.FullEnvironment
import data.app.{AppBroadcast, SessionBroadcast}
import data.app.navigation.NavigationService
import data.storage.StoragePreferences

import presentation.menu.edit.EditMenu
import presentation.menu.file.FileMenu
import presentation.menu.help.HelpMenu
import presentation.menu.view.ViewMenu

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
