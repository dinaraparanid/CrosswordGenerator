package presentation.main

import data.app.AppConfig
import data.app.navigation.{NavigationService, Navigator}
import presentation.navigationService
import zio.ZIO

import javax.swing.JButton

def GenerateButton(): ZIO[AppConfig & NavigationService, Nothing, JButton] =
  def impl(nav: Option[Navigator]): ZIO[AppConfig, Nothing, JButton] =
    MainMenuButton(text = "Generate") { _ ⇒
      nav foreach (_.navigateToGenerateScreen())
    }

  for {
    service ← navigationService()
    ref = service.nav
    nav ← ref.get
    button ← impl(nav)
  } yield button
