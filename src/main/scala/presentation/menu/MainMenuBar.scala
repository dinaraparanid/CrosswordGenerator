package presentation.menu

import data.app.AppConfig
import data.app.navigation.NavigationService

import zio.{RIO, ZIO}

import javax.swing.{JMenu, JMenuBar}

def MainMenuBar(): RIO[AppConfig & NavigationService, JMenuBar] =
  val menu = new JMenuBar:
    add(FileMenu())

  def setContentOfMenu(settingsMenu: JMenu): Unit =
    menu add settingsMenu

  for {
    settingsMenu ← SettingsMenu()
    _            ← ZIO attempt
      setContentOfMenu(settingsMenu)
  } yield menu
