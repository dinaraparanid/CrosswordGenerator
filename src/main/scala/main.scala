import data.app.AppConfig
import presentation.MainFrame
import presentation.ui.Themes
import presentation.ui.theme
import zio.{Promise, ZEnvironment, ZIO, ZIOAppDefault}

object Application extends ZIOAppDefault:
  private val appLogic: ZIO[AppConfig, Throwable, Unit] =
    for {
      _ ← runApplication()
      _ ← waitUntilClosed()
    } yield ()

  override def run: zio.IO[Throwable, Unit] =
    appLogic provideEnvironment initialConfig()

  private def runApplication(): ZIO[AppConfig, Throwable, Unit] =
    for {
      frame ← MainFrame()
      _ ← ZIO attempt (frame setVisible true)
    } yield ()

  private def initialConfig(): ZEnvironment[AppConfig] =
    ZEnvironment[AppConfig](AppConfig(theme = theme(Themes.Dark)))

  private def waitUntilClosed(): ZIO[Any, Nothing, Unit] =
    for {
      closeEffect ← Promise.make[Nothing, Nothing]
      _ ← closeEffect.await
    } yield ()
