package com.paranid5.crossword_generator.data.storage

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import java.awt.Font

import scala.xml.{Elem, Node, XML}

def appFontStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "font",
    default = Font.SERIF,
    predicate = Font.getFont(_) != null
  )

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

private def updatedFont(data: Elem, font: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "font" ⇒ <font>{font}</font>
    case elem: Elem ⇒ updatedFont(elem, font)
    case other ⇒ other
)
