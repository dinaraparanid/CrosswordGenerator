package com.paranid5.crossword_generator.data.storage

import zio.channel.Channel
import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import scala.xml.{Elem, Node, XML}

/**
 * Retrieves a ZIO stream of title inputs from the XML data
 * @return [[UStream]] of crossword titles
 */

def titleInputStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream("title")

/**
 * Retrieves a title input from the XML data
 * @return current crossword title
 */

def titleInput: URIO[StoragePreferences, String] =
  stringData("title")

/**
 * Saves new [[titleInput]] to the disk,
 * then sends broadcast to update the UI
 *
 * @param elem       current XML app config
 * @param updateChan update messages channel
 * @param titleInput title entered by user
 * @return [[RIO]] that completes when UI broadcast is sent
 */

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

/**
 * Updates the title value in the XML data
 *
 * @param data  application XML data
 * @param input title entered by the user
 * @return updated XML data element
 */

private def updatedTitle(data: Elem, input: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "title" ⇒ <title>{input}</title>
    case elem: Elem ⇒ updatedTitle(elem, input)
    case other ⇒ other
)
