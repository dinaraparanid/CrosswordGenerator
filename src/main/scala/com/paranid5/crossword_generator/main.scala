package com.paranid5.crossword_generator

import com.paranid5.crossword_generator.data.FullEnvironment
import com.paranid5.crossword_generator.data.app.navigation.NavigationService
import com.paranid5.crossword_generator.data.app.{AppConfigChannel, SessionChannel}
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, monitorDataChanges, setupTheme}
import com.paranid5.crossword_generator.presentation.MainFrame

import zio.{Promise, RIO, UIO, ULayer, ZIO, ZIOAppDefault}

import java.awt.{Font, GraphicsEnvironment}
import java.io.File

object Application extends ZIOAppDefault:
  /** Full layer with all services combined */

  private val appLayer: ULayer[FullEnvironment] =
    StoragePreferences.layer ++
    AppConfigChannel.layer       ++
    SessionChannel.layer   ++
    NavigationService.layer

  /** Application event loop logic */

  private val appLogic: RIO[FullEnvironment, Unit] =
    setup()

    for
      _ ← runApplication()
      _ ← waitUntilClosed()
    yield ()

  override def run: RIO[Any, Unit] =
    appLogic provideLayer appLayer

  /** Application event loop */

  private def runApplication(): RIO[FullEnvironment, Unit] =
    for
      _     ← setupTheme()
      frame ← MainFrame()
      _     ← monitorDataChanges().fork
      _     ← ZIO attempt (frame setVisible true)
    yield ()

  /** Eternal fibber to keep application shown */

  private def waitUntilClosed(): UIO[Unit] =
    for
      closeEffect ← Promise.make[Nothing, Nothing]
      _           ← closeEffect.await
    yield ()

/**
 * Installs additional fonts
 * and closes output streams
 */

private def setup(): Unit =
  installFonts()
  closeStreams()

/** Installs additional fonts */

private def installFonts(): Unit =
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge registerFont "./res/pristina.ttf"

/** Closes output streams */

private def closeStreams(): Unit =
  System.out.close()
  System.err.close()

extension (ge: GraphicsEnvironment)
  /**
   * Registers font by its path
   *
   * @param path path to the .ttf font file
   * @return true if font is registered
   */

  private def registerFont(path: String): Boolean =
    ge registerFont
      Font.createFont(
        Font.TRUETYPE_FONT,
        File(path)
      )