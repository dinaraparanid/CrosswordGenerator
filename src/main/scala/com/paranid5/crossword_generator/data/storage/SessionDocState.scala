package com.paranid5.crossword_generator.data.storage

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import java.io.File
import scala.xml.{Elem, Node, XML}

private val InitialDocPath: String = "session.pdf"

def sessionDocPathStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "document",
    default = InitialDocPath,
    predicate = doc ⇒ doc.endsWith(".pdf") && File(doc).exists()
  )

def sessionDoc: URIO[StoragePreferences, String] =
  stringData(
    that = "document",
    default = InitialDocPath,
    predicate = doc ⇒ doc.endsWith(".pdf") && File(doc).exists()
  )

def storeSessionDocPath(docPath: String): RIO[StoragePreferences, Unit] =
  for
    elem ← data

    _ ← ZIO attemptBlocking
      XML.save(
        filename = StoragePath,
        node = updatedSessionDoc(elem, docPath),
        enc = "UTF-8",
        xmlDecl = true
      )

    _ ← notifyDataUpdate()
  yield ()

private def updatedSessionDoc(data: Elem, docPath: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "document" ⇒ <document>{docPath}</document>
    case elem: Elem ⇒ updatedSessionDoc(elem, docPath)
    case other ⇒ other
)
