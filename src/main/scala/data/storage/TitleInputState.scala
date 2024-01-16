package data.storage

import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import scala.xml.{Elem, Node, XML}

def titleInputStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream("title")

def titleInput: URIO[StoragePreferences, String] =
  stringData("title")

def storeTitleInput(titleInput: String): RIO[StoragePreferences, Unit] =
  for
    elem ← data

    _ ← ZIO attemptBlocking
      XML.save(
        filename = StoragePath,
        node = updatedTitle(elem, titleInput),
        enc = "UTF-8",
        xmlDecl = true
      )

    _ ← notifyDataUpdate()
  yield ()

private def updatedTitle(data: Elem, input: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "title" ⇒ <title>{input}</title>
    case elem: Elem ⇒ updatedTitle(elem, input)
    case other ⇒ other
)
