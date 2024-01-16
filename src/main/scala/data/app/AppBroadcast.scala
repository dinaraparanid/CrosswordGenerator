package data.app

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*

import data.storage.{StoragePreferences, appTheme, storeAppTheme, toRIO}

import zio.channel.{Channel, foreverWhile}
import zio.stream.{SubscriptionRef, UStream}
import zio.{RIO, ULayer, ZIO, ZLayer}

import java.awt.Font
import javax.swing.{JFrame, SwingUtilities}

final class AppBroadcast(
  private val resetThemeChan: Channel[Boolean]
):
  def resetTheme(): RIO[Any, Unit] =
    (resetThemeChan send true).toRIO

  def startThemeMonitoring(frame: JFrame): RIO[StoragePreferences, Unit] =
    foreverWhile:
      for
        call  ← resetThemeChan.receive.toRIO
        theme ← appTheme

        _ ← storeAppTheme(switchTheme(theme))
        _ ← ZIO attempt (SwingUtilities updateComponentTreeUI frame)
      yield call

  private def switchTheme(currentTheme: String): String =
    currentTheme match
      case FlatMaterialPalenightIJTheme.NAME ⇒
        FlatNightOwlIJTheme.setup()
        FlatNightOwlIJTheme.NAME

      case FlatNightOwlIJTheme.NAME ⇒
        FlatMaterialPalenightIJTheme.setup()
        FlatMaterialPalenightIJTheme.NAME

object AppBroadcast:
  val layer: ULayer[AppBroadcast] =
    ZLayer:
      for resetThemeChan ← Channel.make[Boolean]
        yield AppBroadcast(resetThemeChan)
