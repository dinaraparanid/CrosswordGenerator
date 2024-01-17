package com.paranid5.crossword_generator.data.storage

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import java.awt.Font

import scala.xml.{Elem, Node, XML}

/**
 * Retrieves the app's font changes as a ZIO stream of font values.
 * If no matching elements are found, [[Font.SERIF]] is used
 * @return [[UStream]] of font names applied by the user
 */

def appFontStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "font",
    default = Font.SERIF,
    predicate = Font.getFont(_) != null
  )

/**
 * Saves new [[font]] to the disk,
 * then sends broadcast to update the UI
 * 
 * @return [[RIO]] that completes when UI broadcast is sent
 */

def storeAppFont(font: String): RIO[StoragePreferences, Unit] =
  for
    elem ← data

    _ ← ZIO attemptBlocking
      XML.save(
        filename = StoragePath,
        node = updatedFont(elem, font),
        enc = "UTF-8",
        xmlDecl = true
      )

    _ ← notifyDataUpdate()
  yield ()

/**
 * Updates the font value in the XML data
 *
 * @param data application XML data
 * @param font new font itself
 * @return updated XML data element
 */

private def updatedFont(data: Elem, font: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "font" ⇒ <font>{font}</font>
    case elem: Elem ⇒ updatedFont(elem, font)
    case other ⇒ other
)
