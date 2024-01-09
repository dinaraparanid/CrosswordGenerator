package presentation.main

import data.app.AppConfig
import data.app.navigation.NavigationService

import zio.URIO

import javax.swing.JButton

def GenerateButton(): URIO[AppConfig & NavigationService, JButton] =
  MainMenuButton(text = s"Generate") { (nav, _) ⇒
    nav foreach (_.navigateToGenerateScreen())
  }
