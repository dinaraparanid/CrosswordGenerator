package presentation

import data.app.AppConfig
import data.app.navigation.{NavigationService, Navigator}
import presentation.ui.Theme

import zio.{ZIO, URIO}
import zio.stream.{SubscriptionRef, UStream}

def appConfig(): URIO[AppConfig, AppConfig] =
  for (env ← ZIO.environment[AppConfig])
    yield env.get[AppConfig]

def appThemeRef(): URIO[AppConfig, SubscriptionRef[Theme]] =
  for (config ← appConfig())
    yield config.theme

def appThemeStream(): URIO[AppConfig, UStream[Theme]] =
  for (ref ← appThemeRef())
    yield ref.changes

def appTheme(): URIO[AppConfig, Theme] =
  for {
    ref   ← appThemeRef()
    theme ← ref.get
  } yield theme

def appFontRef(): URIO[AppConfig, SubscriptionRef[String]] =
  for (config ← appConfig())
    yield config.font

def appFontStream(): URIO[AppConfig, UStream[String]] =
  for (ref ← appFontRef())
    yield ref.changes

def navigationService(): URIO[NavigationService, NavigationService] =
  for (nav ← ZIO.service[NavigationService])
    yield nav

def navigatorRef(): URIO[NavigationService, SubscriptionRef[Option[Navigator]]] =
  for (service ← navigationService())
    yield service.nav

def navigatorStream(): URIO[NavigationService, UStream[Option[Navigator]]] =
  for (ref ← navigatorRef())
    yield ref.changes

def navigator(): URIO[NavigationService, Option[Navigator]] =
  for {
    ref ← navigatorRef()
    nav ← ref.get
  } yield nav
