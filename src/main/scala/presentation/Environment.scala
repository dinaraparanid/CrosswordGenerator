package presentation

import data.app.{AppConfig, SessionStates}
import data.app.navigation.{NavigationService, Navigator}

import zio.{URIO, ZIO}
import zio.stream.{SubscriptionRef, UStream}

def appConfig(): URIO[AppConfig, AppConfig] =
  for env ← ZIO.environment[AppConfig]
    yield env.get[AppConfig]

def appFontRef(): URIO[AppConfig, SubscriptionRef[String]] =
  for config ← appConfig()
    yield config.font

def appFontStream(): URIO[AppConfig, UStream[String]] =
  for ref ← appFontRef()
    yield ref.changes

def navigationService(): URIO[NavigationService, NavigationService] =
  for nav ← ZIO.service[NavigationService]
    yield nav

def navigatorRef(): URIO[NavigationService, SubscriptionRef[Option[Navigator]]] =
  for service ← navigationService()
    yield service.nav

def navigatorStream(): URIO[NavigationService, UStream[Option[Navigator]]] =
  for ref ← navigatorRef()
    yield ref.changes

def navigator(): URIO[NavigationService, Option[Navigator]] =
  for {
    ref ← navigatorRef()
    nav ← ref.get
  } yield nav

def sessionStates(): URIO[SessionStates, SessionStates] =
  for ses ← ZIO.service[SessionStates]
    yield ses

def titleInputRef(): URIO[SessionStates, SubscriptionRef[String]] =
  for ses ← sessionStates()
    yield ses.titleInput

def titleInputStream(): URIO[SessionStates, UStream[String]] =
  for ref ← titleInputRef()
    yield ref.changes

def titleInput(): URIO[SessionStates, String] =
  for {
    ref   ← titleInputRef()
    title ← ref.get
  } yield title

def wordsInputRef(): URIO[SessionStates, SubscriptionRef[String]] =
  for ses ← sessionStates()
    yield ses.wordsInput

def wordsInputStream(): URIO[SessionStates, UStream[String]] =
  for ref ← wordsInputRef()
    yield ref.changes

def wordsInput(): URIO[SessionStates, String] =
  for {
    ref   ← wordsInputRef()
    words ← ref.get
  } yield words

def sessionDocRef(): URIO[SessionStates, SubscriptionRef[String]] =
  for ses ← sessionStates()
    yield ses.sessionDoc

def sessionDocStream(): URIO[SessionStates, UStream[String]] =
  for ref ← sessionDocRef()
    yield ref.changes

def sessionDoc(): URIO[SessionStates, String] =
  for {
    ref ← sessionDocRef()
    doc ← ref.get
  } yield doc