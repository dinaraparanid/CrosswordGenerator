import data.FullEnvironment
import data.app.navigation.NavigationService
import data.app.{AppBroadcast, SessionBroadcast}
import data.storage.{StoragePreferences, monitorDataChanges, setupTheme}
import presentation.MainFrame

import zio.{Promise, RIO, Scope, UIO, ULayer, ZIO, ZIOAppDefault}

import java.awt.{Font, GraphicsEnvironment}
import java.io.File
import javax.swing.JFrame

object Application extends ZIOAppDefault:
  private val appLayer: ULayer[FullEnvironment & Scope] =
    StoragePreferences.layer ++
    AppBroadcast.layer       ++
    SessionBroadcast.layer   ++
    NavigationService.layer  ++
    Scope.default

  private val appLogic: RIO[FullEnvironment & Scope, Unit] =
    setup()

    for
      _ ← runApplication()
      _ ← waitUntilClosed()
    yield ()

  override def run: RIO[Any, Unit] =
    appLogic provideLayer appLayer

  private def runApplication(): RIO[FullEnvironment & Scope, JFrame] =
    for
      _     ← setupTheme()
      frame ← MainFrame()
      _     ← monitorDataChanges().fork
      _     ← ZIO attempt (frame setVisible true)
    yield frame

  private def waitUntilClosed(): UIO[Unit] =
    for
      closeEffect ← Promise.make[Nothing, Nothing]
      _           ← closeEffect.await
    yield ()

private def setup(): Unit =
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge registerFont "./res/pristina.ttf"
  System.err.close()

extension (ge: GraphicsEnvironment)
  private def registerFont(path: String) =
    ge registerFont
      Font.createFont(
        Font.TRUETYPE_FONT,
        File(path)
      )