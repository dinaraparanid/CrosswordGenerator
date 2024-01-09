package presentation.main.menu

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme
import presentation.ui.utils.combine

import zio.{RIO, ZIO}

import javax.swing.{JMenu, JMenuBar}

def MainMenuBar(): RIO[AppConfig, JMenuBar] =
  def impl(
    fileMenu:     JMenu,
    settingsMenu: JMenu,
  ): JMenuBar =
    new JMenuBar:
      setOpaque(true)
      add(fileMenu)
      add(settingsMenu)

  def recompose(menuBar: JMenuBar, theme: Theme): Unit =
    menuBar setBackground theme.backgroundColor.darker()
    menuBar setForeground theme.fontColor

  for {
    fileMenu     ← FileMenu()
    settingsMenu ← SettingsMenu()
    themes       ← appThemeStream()

    menu = impl(fileMenu, settingsMenu)
    _    ← themes.foreach(ZIO attempt recompose(menu, _)).fork
  } yield menu
