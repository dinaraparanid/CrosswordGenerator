package presentation.menu

import data.app.{AppConfig, SessionStates}
import data.app.navigation.NavigationService

import presentation.menu.edit.EditMenu
import presentation.menu.file.FileMenu
import presentation.menu.help.HelpMenu
import presentation.menu.view.ViewMenu

import zio.{RIO, ZIO}

import javax.swing.{JMenu, JMenuBar}

def MainMenuBar(): RIO[AppConfig & NavigationService & SessionStates, JMenuBar] =
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
