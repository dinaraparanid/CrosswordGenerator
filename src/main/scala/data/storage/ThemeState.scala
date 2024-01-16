package data.storage

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import scala.xml.{Elem, Node, XML}

private lazy val ThemeSet = Set(
  FlatMaterialPalenightIJTheme.NAME,
  FlatNightOwlIJTheme.NAME
)

def appThemeStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "theme",
    default = FlatMaterialPalenightIJTheme.NAME,
    predicate = themeExists
  )

def appTheme: URIO[StoragePreferences, String] =
  stringData(
    that = "theme",
    default = FlatMaterialPalenightIJTheme.NAME,
    predicate = themeExists
  )

def setupTheme(): URIO[StoragePreferences, Boolean] =
  for theme ← appTheme
    yield setupLaf(theme)

private def setupLaf: String ⇒ Boolean =
  case FlatMaterialPalenightIJTheme.NAME ⇒
    FlatMaterialPalenightIJTheme.setup()

  case FlatNightOwlIJTheme.NAME ⇒
    FlatNightOwlIJTheme.setup()

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

private def themeExists(theme: String): Boolean =
  ThemeSet contains theme

private def updatedTheme(data: Elem, theme: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "theme" ⇒ <theme>{theme}</theme>
    case elem: Elem ⇒ updatedTheme(elem, theme)
    case other ⇒ other
)
