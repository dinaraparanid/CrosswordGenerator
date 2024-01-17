package com.paranid5.crossword_generator.data.storage

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import scala.xml.{Elem, Node, XML}

private lazy val ThemeSet = Set(
  FlatMaterialPalenightIJTheme.NAME,
  FlatNightOwlIJTheme.NAME
)

/**
 * Provides a ZIO stream of applied themes from the XML data.
 * [[FlatMaterialPalenightIJTheme.NAME]] is applied if no theme is found
 *
 * @return [[UStream]] of themes' names
 */

def appThemeStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "theme",
    default = FlatMaterialPalenightIJTheme.NAME,
    predicate = isThemeSupported
  )

/**
 * Provides currently applied theme from the XML data.
 * [[FlatMaterialPalenightIJTheme.NAME]] is applied if no theme is found
 *
 * @return currently applied theme's name
 */

def appTheme: URIO[StoragePreferences, String] =
  stringData(
    that = "theme",
    default = FlatMaterialPalenightIJTheme.NAME,
    predicate = isThemeSupported
  )

/**
 * Applies the currently selected theme,
 * updating the whole UI of the app
 *
 * @return true if theme is applied
 */

def setupTheme(): URIO[StoragePreferences, Boolean] =
  for theme ← appTheme
    yield setupLaf(theme)

/**
 * Applies the currently selected theme,
 * updating the whole UI of the app
 *
 * @return true if theme is applied
 */

private def setupLaf: String ⇒ Boolean =
  case FlatMaterialPalenightIJTheme.NAME ⇒
    FlatMaterialPalenightIJTheme.setup()

  case FlatNightOwlIJTheme.NAME ⇒
    FlatNightOwlIJTheme.setup()

/**
 * Saves new [[theme]] to the disk,
 * then sends broadcast to update the UI
 *
 * @return RIO that completes when UI broadcast is sent
 */

def storeAppTheme(theme: String): RIO[StoragePreferences, Unit] =
  for
    elem ← data

    _ ← ZIO attemptBlocking
      XML.save(
        filename = StoragePath,
        node = updatedTheme(elem, theme),
        enc = "UTF-8",
        xmlDecl = true
      )

    _ ← notifyDataUpdate()
  yield ()

/**
 * Checks if the specified theme
 * is supported by the application
 *
 * @param theme theme's name
 * @return true if theme is supported
 */

private def isThemeSupported(theme: String): Boolean =
  ThemeSet contains theme

/**
 * Updates the theme value in the XML data
 *
 * @param data  application XML data
 * @param theme new theme itself
 * @return updated XML data element
 */

private def updatedTheme(data: Elem, theme: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "theme" ⇒ <theme>{theme}</theme>
    case elem: Elem ⇒ updatedTheme(elem, theme)
    case other ⇒ other
)
