import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightIJTheme

import data.app.navigation.NavigationService
import data.app.{AppConfig, InputStates}
import presentation.MainFrame

import zio.{Promise, RIO, UIO, ULayer, ZIO, ZIOAppDefault}

import java.awt.{Font, GraphicsEnvironment}
import java.io.File
import javax.swing.JFrame

object Application extends ZIOAppDefault:
  private val appLayer: ULayer[AppConfig & NavigationService & InputStates] =
    AppConfig.layer ++ NavigationService.layer ++ InputStates.layer

  private val appLogic: RIO[AppConfig & NavigationService & InputStates, Unit] =
    setup()

    for {
      _ ← runApplication()
      _ ← waitUntilClosed()
    } yield ()

  override def run: RIO[Any, Unit] =
    appLogic provideLayer appLayer

  private def runApplication(): RIO[AppConfig & NavigationService & InputStates, JFrame] =
    for {
      frame ← MainFrame()
      _     ← ZIO attempt (frame setVisible true)
    } yield frame

  private def waitUntilClosed(): UIO[Unit] =
    for {
      closeEffect ← Promise.make[Nothing, Nothing]
      _           ← closeEffect.await
    } yield ()

private def setup(): Unit =
  FlatMaterialPalenightIJTheme.setup()
  System.err.close()

  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge registerFont "./res/pristina.ttf"

extension (ge: GraphicsEnvironment)
  private def registerFont(path: String) =
    ge registerFont
      Font.createFont(
        Font.TRUETYPE_FONT,
        File(path)
      )