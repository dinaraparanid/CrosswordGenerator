package com.paranid5.crossword_generator.presentation

import com.paranid5.crossword_generator.data.app.navigation.{NavigationService, Navigator}
import com.paranid5.crossword_generator.data.app.{AppConfigChannel, SessionChannel}

import zio.channel.Channel
import zio.stream.{SubscriptionRef, UStream}
import zio.{URIO, ZIO}

/**
 * Retrieves [[AppConfigChannel]] from the current ZIO environment
 * @return [[URIO]] that completes when the config is retrieved
 */

def appConfigBroadcast(): URIO[AppConfigChannel, AppConfigChannel] =
  for app ← ZIO.service[AppConfigChannel]
    yield app

/**
 * Retrieves [[NavigationService]] from the current ZIO environment
 * @return [[URIO]] that completes when the service is retrieved
 */

def navigationService(): URIO[NavigationService, NavigationService] =
  for nav ← ZIO.service[NavigationService]
    yield nav

/**
 * Retrieves hot-reload [[Navigator]] state from the current ZIO environment
 * @return [[URIO]] that completes when the state is retrieved
 */

def navigatorRef(): URIO[NavigationService, SubscriptionRef[Option[Navigator]]] =
  for service ← navigationService()
    yield service.nav

/**
 * Produces [[Navigator]] stream from the current ZIO environment
 * @return [[UStream]] from navigator's changes
 */

def navigatorStream(): URIO[NavigationService, UStream[Option[Navigator]]] =
  for ref ← navigatorRef()
    yield ref.changes

/**
 * Retrieves current [[Navigator]] from the current ZIO environment
 * @return [[URIO]] that completes when the navigator is retrieved
 */

def navigator(): URIO[NavigationService, Option[Navigator]] =
  for
    ref ← navigatorRef()
    nav ← ref.get
  yield nav

/**
 * Retrieves [[SessionChannel]] from the current ZIO environment
 * @return [[URIO]] that completes when the session is retrieved
 */

def sessionChannel(): URIO[SessionChannel, SessionChannel] =   
  for ses ← ZIO.service[SessionChannel]
    yield ses

/**
 * Retrieves update page channel from the current ZIO environment
 * @return [[URIO]] that completes when the channel is retrieved
 */

def updatePageChannel(): URIO[SessionChannel, Channel[Boolean]] =
  for ses ← sessionChannel()
    yield ses.updPageChan