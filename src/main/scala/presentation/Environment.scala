package presentation

import data.app.AppConfig
import data.app.navigation.{NavigationService, Navigator}
import presentation.ui.Theme
import zio.ZIO

def appConfig(): ZIO[AppConfig, Nothing, AppConfig] =
  for {
    env ← ZIO.environment[AppConfig]
    config = env.get[AppConfig]
  } yield config

def appTheme(): ZIO[AppConfig, Nothing, Theme] =
  for {
    config ← appConfig()
    theme = config.theme
  } yield theme

def appFont(): ZIO[AppConfig, Nothing, String] =
  for {
    config ← appConfig()
    font = config.font
  } yield font

def navigationService(): ZIO[NavigationService, Nothing, NavigationService] =
  for {
    nav ← ZIO.service[NavigationService]
  } yield nav

def navigatorOption(): ZIO[NavigationService, Nothing, Option[Navigator]] =
  for {
    service ← navigationService()
    ref = service.nav
    navigator ← ref.get
  } yield navigator

def navigator(): ZIO[NavigationService, Nothing, Navigator] =
  for {
    nav ← navigatorOption()
  } yield nav.get
