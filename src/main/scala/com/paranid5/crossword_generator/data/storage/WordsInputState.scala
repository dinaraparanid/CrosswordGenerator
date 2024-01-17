package com.paranid5.crossword_generator.data.storage

import zio.channel.Channel
import zio.stm.TReentrantLock
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

/**
 * Retrieves a ZIO stream of crossword
 * words and meanings input from the XML data
 *
 * @return [[UStream]] of crossword words with meanings
 */

def wordsInputStream: URIO[StoragePreferences, UStream[String]] =
  stringDataStream(
    that = "words",
    default = WordsInitialText,
    predicate = !_.isBlank
  )

/**
 * Retrieves a crossword words
 * and meanings input from the XML data
 *
 * @return current crossword words with meanings
 */

def wordsInput: URIO[StoragePreferences, String] =
  stringData(
    that = "words",
    default = WordsInitialText,
    predicate = !_.isBlank
  )

/**
 * Produces a ZIO stream based on [[wordsInputStream]]
 * that checks if words input is valid
 *
 * @return [[UStream]] of correctness states
 */

def isInputCorrectStream: URIO[StoragePreferences, UStream[Boolean]] =
  wordsInputStream map:
    _ map CrosswordCorrectInputRegex.matches

/**
 * Saves new [[wordsInput]] to the disk,
 * then sends broadcast to update the UI
 *
 * @param elem       current XML app config
 * @param updateChan update messages channel
 * @param wordsInput words with meanings entered by user
 * @return [[RIO]] that completes when UI broadcast is sent
 */

def storeWordsInput(
  elem:        Elem,
  updateChan:  Channel[Boolean],
  storageLock: TReentrantLock,
  wordsInput:  String
): RIO[Any, Unit] =
  ZIO scoped:
    for
      _ ← storageLock.writeLock

      _ ← ZIO attemptBlocking
        XML.save(
          filename = StoragePath,
          node = updatedWords(elem, wordsInput),
          enc = "UTF-8",
          xmlDecl = true
        )

      _ ← notifyDataUpdate(updateChan)
    yield ()

/**
 * Updates the title value in the XML data
 *
 * @param data  application XML data
 * @param input words entered by the user
 * @return updated XML data element
 */

private def updatedWords(data: Elem, input: String): Elem = data.copy(
  child = data.child.map:
    case elem: Elem if elem.label == "words" ⇒ <words>{input}</words>
    case elem: Elem ⇒ updatedWords(elem, input)
    case other ⇒ other
)