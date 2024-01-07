import data.app.AppConfig
import data.app.navigation.NavigationService
import presentation.MainFrame
import zio.{Promise, ZIO, ZIOAppDefault}

object Application extends ZIOAppDefault:
  private val appLogic: ZIO[AppConfig & NavigationService, Throwable, Unit] =
    for {
      _ ← runApplication()
      _ ← waitUntilClosed()
    } yield ()

  private val appLayer: zio.ULayer[AppConfig & NavigationService] =
    AppConfig.layer ++ NavigationService.layer

  override def run: zio.IO[Throwable, Unit] =
    appLogic provideLayer appLayer

  private def runApplication(): ZIO[AppConfig & NavigationService, Throwable, Unit] =
    for {
      frame ← MainFrame()
      _ ← ZIO attempt (frame setVisible true)
    } yield ()

  private def waitUntilClosed(): ZIO[Any, Nothing, Unit] =
    for {
      closeEffect ← Promise.make[Nothing, Nothing]
      _ ← closeEffect.await
    } yield ()
