import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlIJTheme

import data.app.AppConfig
import data.app.navigation.NavigationService
import presentation.ui.Theme
import presentation.{MainFrame, appThemeStream}

import zio.{Promise, RIO, UIO, ULayer, ZIO, ZIOAppDefault}

import java.awt.{Font, GraphicsEnvironment}
import java.io.File
import javax.swing.{JFrame, UIDefaults, UIManager}

object Application extends ZIOAppDefault:
  private val appLayer: ULayer[AppConfig & NavigationService] =
    AppConfig.layer ++ NavigationService.layer

  private val appLogic: RIO[AppConfig & NavigationService, Unit] =
    setup()

    for {
      themes ← appThemeStream()

      _ ← themes
        .mapZIO { theme ⇒
          setupColors(theme)
          runApplication()
        }
        .sliding(2)
        .foreach { frames ⇒
          ZIO attempt (frames.head setVisible false)
        }

      _ ← waitUntilClosed()
    } yield ()

  override def run: RIO[Any, Unit] =
    appLogic provideLayer appLayer

  private def runApplication(): RIO[AppConfig & NavigationService, JFrame] =
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
  FlatNightOwlIJTheme.setup()
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, File("./res/pristina.ttf")))

private def setupColors(theme: Theme): Unit =
  uiConfig.put("MenuItem.acceleratorForeground", theme.secondaryColor)
  uiConfig.put("MenuItem.acceleratorSelectionForeground", theme.secondaryColor)
  uiConfig.put("MenuItem.selectionBackground", theme.backgroundColor)
  uiConfig.put("MenuItem.selectionForeground", theme.fontColor)
  uiConfig.put("MenuBar.selectionBackground", theme.backgroundColor)
  uiConfig.put("MenuBar.selectionForeground", theme.fontColor)

private def uiConfig: UIDefaults =
  UIManager.getLookAndFeelDefaults