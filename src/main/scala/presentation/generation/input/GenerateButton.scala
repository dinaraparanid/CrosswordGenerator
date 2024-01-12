package presentation.generation.input

import presentation.sessionStates
import presentation.ui.utils.{combine, removeActionListeners}

import data.app.{SessionStates, isInputCorrectStream}
import data.generation.population.TableState

import domain.generation.generation
import domain.session.{SessionDocumentWriter, parsedWordsWithMeanings}
import domain.session.packing.packed

import cats.implicits.*

import com.itextpdf.layout.element.Paragraph

import zio.channel.Channel
import zio.{Runtime, UIO, URIO, RIO, Unsafe, ZIO}

import javax.swing.JButton

import scala.util.Using

def GenerateButton(): URIO[SessionStates, JButton] =
  val button = new JButton("Generate"):
    putClientProperty("JButton.buttonType", "roundRect")

  val runtime = Runtime.default

  def recompose(
    isCrosswordCorrect: Boolean,
    titleInput:         String,
    wordsInput:         String,
    sessionDoc:         String,
    pageChan:           Channel[Boolean]
  ): Unit =
    button setEnabled isCrosswordCorrect
    button.removeActionListeners()
    button addActionListener: _ ⇒
      Unsafe.unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          for {
            table ← generateCrossword(wordsInput)
            _     ← storeCrossword(
              docPath = sessionDoc,
              titleInput = titleInput,
              tableState = table,
              pageChan = pageChan
            )
          } yield ()
      }

  for
    session           ← sessionStates()
    isCorrectStream   = session.isInputCorrectStream
    titleInputStream  = session.titleInput.changes
    wordsInputsStream = session.wordsInput.changes
    sessionDocStream  = session.sessionDoc.changes
    pageChan          = session.pageChan

    _ ← combine(
      isCorrectStream,
      titleInputStream,
      wordsInputsStream,
      sessionDocStream
    )
      .foreach { case (correct, title, words, doc) ⇒
        ZIO attempt recompose(correct, title, words, doc, pageChan)
      }
      .fork
  yield button

private def generateCrossword(wordsInput: String): UIO[TableState] =
  val wordsWithMeanings = parsedWordsWithMeanings(wordsInput)
  val words = wordsWithMeanings.keys.toList
  val tabSize = requiredTableSize(words)
  packed(generation(words, tabSize))

private def requiredTableSize(words: Iterable[String]): Int =
  val maxLength = words.map(_.length).max
  val size = words.size
  math.max(maxLength, size) * 2

private def storeCrossword(
  docPath:    String,
  titleInput: String,
  tableState: TableState,
  pageChan:   Channel[Boolean]
): RIO[Any, Unit] =
  Using(SessionDocumentWriter(docPath)) { doc ⇒
    doc add Paragraph(titleInput)
  }

  (pageChan send true) mapError (err ⇒ Exception(err.toString))
