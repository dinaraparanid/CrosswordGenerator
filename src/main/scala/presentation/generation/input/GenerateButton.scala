package presentation.generation.input

import presentation.sessionStates
import presentation.ui.utils.{removeActionListeners, combine}
import data.app.{SessionStates, isInputCorrectStream}
import data.generation.population.TableState
import domain.generation.generation
import domain.session.parsedWordsWithMeanings

import cats.implicits.*

import zio.{URIO, ZIO}

import javax.swing.JButton

def GenerateButton(): URIO[SessionStates, JButton] =
  val button = new JButton("Generate"):
    putClientProperty("JButton.buttonType", "roundRect")

  def recompose(isCrosswordCorrect: Boolean, wordsInput: String): Unit =
    button setEnabled isCrosswordCorrect
    button.removeActionListeners()
    button addActionListener: _ ⇒
      val table = generateCrossword(wordsInput)
      println(table.show)

  for {
    inputs            ← sessionStates()
    isCorrectStream   = inputs.isInputCorrectStream
    wordsInputsStream = inputs.wordsInput.changes

    _ ← combine(isCorrectStream, wordsInputsStream)
      .foreach { case (correct, words) ⇒
        ZIO attempt recompose(correct, words)
      }
      .fork
  } yield button

private def generateCrossword(wordsInput: String): TableState =
  val wordsWithMeanings = parsedWordsWithMeanings(wordsInput)
  val words = wordsWithMeanings.keys.toList
  val tabSize = requiredTableSize(words)
  generation(words, tabSize)

private def requiredTableSize(words: Iterable[String]): Int =
  val maxLength = words.map(_.length).max
  val size = words.size
  math.max(maxLength, size) * 2
