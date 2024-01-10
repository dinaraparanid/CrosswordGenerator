package presentation

import data.app.{AppConfig, InputStates}
import data.app.navigation.{NavigationService, Navigator}

import zio.{URIO, ZIO}
import zio.stream.{SubscriptionRef, UStream}

def appConfig(): URIO[AppConfig, AppConfig] =
  for (env ← ZIO.environment[AppConfig])
    yield env.get[AppConfig]

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

def inputStates(): URIO[InputStates, InputStates] =
  for (inp ← ZIO.service[InputStates])
    yield inp

def titleInputRef(): URIO[InputStates, SubscriptionRef[String]] =
  for (inp ← inputStates())
    yield inp.titleInput

def titleInputStream(): URIO[InputStates, UStream[String]] =
  for (ref ← titleInputRef())
    yield ref.changes

def wordsInputRef(): URIO[InputStates, SubscriptionRef[String]] =
  for (inp ← inputStates())
    yield inp.wordsInput

def wordsInputStream(): URIO[InputStates, UStream[String]] =
  for (ref ← wordsInputRef())
    yield ref.changes
