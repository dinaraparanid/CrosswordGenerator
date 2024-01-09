package presentation.main

import data.app.AppConfig
import data.app.navigation.NavigationService

import zio.URIO

import javax.swing.JButton

def SettingsButton(): URIO[AppConfig & NavigationService, JButton] =
  MainMenuButton(text = "Settings") { (nav, _) ⇒
    nav foreach (_.navigateToSettingsScreen())
  }
