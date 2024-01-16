package com.paranid5.crossword_generator.data.storage

import zio.channel.Channel
import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import scala.xml.{Elem, Node, XML}

def titleInputStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream("title")

def titleInput: URIO[StoragePreferences, String] =
  stringData("title")

def storeTitleInput(
  elem:       Elem,
  updateChan: Channel[Boolean],
  titleInput: String
): RIO[Any, Unit] =
  for
    _ ← ZIO attemptBlocking
      XML.save(
        filename = StoragePath,
        node = updatedTitle(elem, titleInput),
        enc = "UTF-8",
        xmlDecl = true
      )

    _ ← notifyDataUpdate(updateChan)
  yield ()

private def updatedTitle(data: Elem, input: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "title" ⇒ <title>{input}</title>
    case elem: Elem ⇒ updatedTitle(elem, input)
    case other ⇒ other
)
