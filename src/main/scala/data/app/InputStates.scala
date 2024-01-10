package data.app

import zio.{UIO, ULayer, ZLayer}
import zio.stream.{SubscriptionRef, UStream}

import scala.util.matching.Regex

private val CrosswordCorrectInputRegex: Regex =
  "\\s*((.*) - (.*)\\s*)+".r

case class InputStates(
  titleInput: SubscriptionRef[String],
  wordsInput: SubscriptionRef[String]
)

object InputStates:
  val layer: ULayer[InputStates] =
    ZLayer:
      for {
        title ← SubscriptionRef make ""
        words ← SubscriptionRef make ""
      } yield InputStates(title, words)

extension (states: InputStates)
  def resetTitle(title: String): UIO[Unit] =
    states.titleInput set title

  def resetWords(words: String): UIO[Unit] =
    states.wordsInput set words

  def isInputCorrectStream: UStream[Boolean] =
    states.wordsInput.changes map
      CrosswordCorrectInputRegex.matches