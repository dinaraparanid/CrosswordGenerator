package presentation.menu

import data.app.AppConfig
import data.app.navigation.NavigationService

import zio.{RIO, ZIO}

import javax.swing.{JMenu, JMenuBar}

def MainMenuBar(): RIO[AppConfig & NavigationService, JMenuBar] =
  val menu = new JMenuBar:
    add(FileMenu())
    add(EditMenu())

  def setContentOfMenu(viewMenu: JMenu): Unit =
    menu add viewMenu
    menu add HelpMenu()

  for {
    viewMenu ← ViewMenu()
    _        ← ZIO attempt
      setContentOfMenu(viewMenu)
  } yield menu
