package presentation.main

import data.app.AppConfig
import data.app.navigation.{NavigationService, Navigator}
import presentation.navigationService
import zio.ZIO

import javax.swing.JButton

def SettingsButton(): ZIO[AppConfig & NavigationService, Nothing, JButton] =
  def impl(nav: Option[Navigator]): ZIO[AppConfig, Nothing, JButton] =
    MainMenuButton(text = "Settings") { _ ⇒
      nav foreach (_.navigateToSettingsScreen())
    }

  for {
    service ← navigationService()
    ref = service.nav
    nav ← ref.get
    button ← impl(nav)
  } yield button

