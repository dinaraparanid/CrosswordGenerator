package com.paranid5.crossword_generator.data.storage

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import java.io.File
import scala.xml.{Elem, Node, XML}

private val InitialDocPath: String = "session.pdf"

/**
 * Retrieves the session's document path as a ZIO stream of document paths.
 * If document file does not exist or it isn't pdf, [[InitialDocPath]] is used
 * @return [[UStream]] of session document path' changes
 */

def sessionDocPathStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "document",
    default = InitialDocPath,
    predicate = doc ⇒ doc.endsWith(".pdf") && File(doc).exists()
  )

/**
 * Retrieves the session's document path.
 * If document file does not exist or
 * it isn't pdf, [[InitialDocPath]] is used
 * @return [[URIO]] with current session document path
 */

def sessionDoc: URIO[StoragePreferences, String] =
  stringData(
    that = "document",
    default = InitialDocPath,
    predicate = doc ⇒ doc.endsWith(".pdf") && File(doc).exists()
  )

/**
 * Saves session document path to the disk,
 * then sends broadcast to update the UI
 * @return [[RIO]] that completes when UI broadcast is sent
 */

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

/**
 * Updates the session document path in the XML data
 *
 * @param data application XML data
 * @param docPath new session document path itself
 * @return updated XML data element
 */

private def updatedSessionDoc(data: Elem, docPath: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "document" ⇒ <document>{docPath}</document>
    case elem: Elem ⇒ updatedSessionDoc(elem, docPath)
    case other ⇒ other
)
