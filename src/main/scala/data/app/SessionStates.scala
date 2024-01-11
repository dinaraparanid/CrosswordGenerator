package data.app

import domain.session.sessionDocumentPath

import zio.stream.{SubscriptionRef, UStream}
import zio.{UIO, ULayer, ZLayer}

import scala.util.matching.Regex

private val CrosswordCorrectInputRegex: Regex =
  "\\s*((.*) - (.*)\\s*){2,}".r

case class SessionStates(
  titleInput: SubscriptionRef[String],
  wordsInput: SubscriptionRef[String],
  sessionDoc: SubscriptionRef[String]
)

object SessionStates:
  val layer: ULayer[SessionStates] =
    ZLayer:
      for {
        title ← SubscriptionRef make ""
        words ← SubscriptionRef make ""
        doc   ← SubscriptionRef make sessionDocumentPath
      } yield SessionStates(title, words, doc)

extension (states: SessionStates)
  def resetTitle(title: String): UIO[Unit] =
    states.titleInput set title

  def resetWords(words: String): UIO[Unit] =
    states.wordsInput set words

  def isInputCorrectStream: UStream[Boolean] =
    states.wordsInput.changes map
      CrosswordCorrectInputRegex.matches