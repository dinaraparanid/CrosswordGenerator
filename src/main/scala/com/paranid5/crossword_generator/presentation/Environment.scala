package com.paranid5.crossword_generator.presentation

import com.paranid5.crossword_generator.data.app.{AppBroadcast, SessionBroadcast}
import com.paranid5.crossword_generator.data.app.navigation.{NavigationService, Navigator}
import com.paranid5.crossword_generator.data.storage.StoragePreferences

import zio.{URIO, ZIO}
import zio.channel.Channel
import zio.stream.{SubscriptionRef, UStream}

def appBroadcast(): URIO[AppBroadcast, AppBroadcast] =
  for env ← ZIO.environment[AppBroadcast]
    yield env.get[AppBroadcast]

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
  for
    ref ← navigatorRef()
    nav ← ref.get
  yield nav

def sessionBroadcast(): URIO[SessionBroadcast, SessionBroadcast] =   
  for ses ← ZIO.service[SessionBroadcast]
    yield ses

def updatePageChannel(): URIO[SessionBroadcast, Channel[Boolean]] =
  for ses ← sessionBroadcast()
    yield ses.updPageChan