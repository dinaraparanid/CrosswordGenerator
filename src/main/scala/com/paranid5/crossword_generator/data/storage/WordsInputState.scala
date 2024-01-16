package com.paranid5.crossword_generator.data.storage

import zio.channel.Channel
import zio.{RIO, URIO, ZIO}
import zio.stream.UStream

import scala.util.matching.Regex
import scala.xml.{Elem, XML}

private val CrosswordCorrectInputRegex: Regex =
  "\\s*((.*) - (.*)\\s*){2,}".r

private val WordsInitialText: String =
  """Type or paste your words here
    |
    |Example:
    |Chieftain - the leader of the Cossacks.
    |Minotaur - Cretan monster with the body of a man and the head of a bull, who lived in a Labyrinth and was killed by Theseus.
    |Scimitar - bladed stabbing and slashing edged weapon with a long single-edged blade having a double bend; something between a saber and a cleaver.""".stripMargin

def wordsInputStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "words",
    default = WordsInitialText,
    predicate = !_.isBlank
  )

def wordsInput: URIO[StoragePreferences, String] =
  stringData(
    that = "words",
    default = WordsInitialText,
    predicate = !_.isBlank
  )

def isInputCorrectStream: URIO[StoragePreferences, UStream[Boolean]] =
  wordsInputStream map:
    _ map CrosswordCorrectInputRegex.matches

def storeWordsInput(
  elem:       Elem,
  updateChan: Channel[Boolean],
  wordsInput: String
): RIO[Any, Unit] =
  for
    _ ← ZIO attemptBlocking
      XML.save(
        filename = StoragePath,
        node = updatedWords(elem, wordsInput),
        enc = "UTF-8",
        xmlDecl = true
      )

    _ ← notifyDataUpdate(updateChan)
  yield ()

private def updatedWords(data: Elem, input: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "words" ⇒ <words>{input}</words>
    case elem: Elem ⇒ updatedWords(elem, input)
    case other ⇒ other
)