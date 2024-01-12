package presentation.generation.input

import presentation.sessionStates
import presentation.ui.utils.{combine, removeActionListeners}

import data.app.{SessionStates, isInputCorrectStream}
import data.generation.population.TableState

import domain.generation.generation
import domain.session.parsedWordsWithMeanings
import domain.session.packing.packed

import cats.implicits.*

import zio.{URIO, UIO, ZIO, Runtime, Unsafe}

import javax.swing.JButton

def GenerateButton(): URIO[SessionStates, JButton] =
  val button = new JButton("Generate"):
    putClientProperty("JButton.buttonType", "roundRect")

  val runtime = Runtime.default

  def recompose(isCrosswordCorrect: Boolean, wordsInput: String): Unit =
    button setEnabled isCrosswordCorrect
    button.removeActionListeners()
    button addActionListener: _ ⇒
      Unsafe.unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          for table ← generateCrossword(wordsInput)
            yield println(table.show)
      }

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

private def generateCrossword(wordsInput: String): UIO[TableState] =
  val wordsWithMeanings = parsedWordsWithMeanings(wordsInput)
  val words = wordsWithMeanings.keys.toList
  val tabSize = requiredTableSize(words)
  packed(generation(words, tabSize))

private def requiredTableSize(words: Iterable[String]): Int =
  val maxLength = words.map(_.length).max
  val size = words.size
  math.max(maxLength, size) * 2
