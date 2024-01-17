package com.paranid5.crossword_generator.data.app

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*

import com.paranid5.crossword_generator.data.storage.{StoragePreferences, appTheme, storeAppTheme}
import com.paranid5.crossword_generator.data.utils.toRIO

import zio.channel.{Channel, foreverWhile}
import zio.{RIO, ULayer, ZIO, ZLayer}

import javax.swing.{JFrame, SwingUtilities}

/**
 * Provides a service for broadcasting application configurations' changes
 * @param resetThemeChan the channel to receive theme reset requests
 */

final class AppConfigChannel(
  private val resetThemeChan: Channel[Boolean]
) extends AnyVal:
  /**
   * Schedules an update to switch theme
   * @return a RIO that will complete when the message is sent
   */

  def resetTheme(): RIO[Any, Unit] =
    (resetThemeChan send true).toRIO

  /**
   * Starts monitoring the theme and updating the UI accordingly
   *
   * @param frame the main frame
   * @return a RIO that will never completes, 
   *         it must be executed in the different fibber
   * @see [[ZIO.fork]]
   */

  def startThemeMonitoring(frame: JFrame): RIO[StoragePreferences, Unit] =
    foreverWhile:
      for
        call  ← resetThemeChan.receive.toRIO
        theme ← appTheme

        _ ← storeAppTheme(switchTheme(theme))
        _ ← ZIO attempt (SwingUtilities updateComponentTreeUI frame)
      yield call

  /**
   * Switches the UI theme between 'Pale Night' and 'Night Owl'
   * @param currentTheme currently applied theme
   * @return the new theme's name
   */

  private def switchTheme(currentTheme: String): String =
    currentTheme match
      case FlatMaterialPalenightIJTheme.NAME ⇒
        FlatNightOwlIJTheme.setup()
        FlatNightOwlIJTheme.NAME

      case FlatNightOwlIJTheme.NAME ⇒
        FlatMaterialPalenightIJTheme.setup()
        FlatMaterialPalenightIJTheme.NAME

/** Provides a layer for the [[AppConfigChannel]] */

object AppConfigChannel:
  /**
   * Creates a ZIO layer for the AppConfigChannel
   * @return a [[ULayer]] for the [[AppConfigChannel]]
   */

  lazy val layer: ULayer[AppConfigChannel] =
    ZLayer:
      for resetThemeChan ← Channel.make[Boolean]
        yield AppConfigChannel(resetThemeChan)
