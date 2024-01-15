package presentation.generation.input

import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.properties.AreaBreakType

import data.app.{SessionStates, isInputCorrectStream}
import data.generation.population.TableState

import domain.generation.generation
import domain.session.packing.packed
import domain.session.{SessionDocumentWriter, parsedWordsWithMeanings}

import presentation.generation.pdf.*
import presentation.sessionStates
import presentation.ui.utils.{combine, removeActionListeners}

import zio.channel.Channel
import zio.{RIO, Runtime, UIO, URIO, Unsafe, ZIO}

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
      Unsafe.unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            for
              tabWithMeans ← generateCrossword(wordsInput)
              (table, meanings) = tabWithMeans

              _ ← showCrossword(
                docPath = sessionDoc,
                titleInput = titleInput,
                tableState = table,
                wordsWithMeanings = meanings,
                pageChan = pageChan
              )
            yield ()

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
      .foreach:
        case (correct, title, words, doc) ⇒
          ZIO attempt recompose(correct, title, words, doc, pageChan)
      .fork
  yield button

private def generateCrossword(wordsInput: String): UIO[(TableState, Map[String, String])] =
  for
    wordsWithMeanings ← ZIO succeedBlocking
      parsedWordsWithMeanings(wordsInput)

    (words, meanings) = wordsWithMeanings

    tabSize ← ZIO succeedBlocking
      requiredTableSize(words)

    table ← ZIO succeedBlocking
      generation(words, tabSize)

    packedTab ← packed(table)
  yield (packedTab, meanings)

private def requiredTableSize(words: Iterable[String]): Int =
  val maxLength = words.map(_.length).max
  val size = words.size
  math.max(maxLength, size) * 2

private def showCrossword(
  docPath:           String,
  titleInput:        String,
  tableState:        TableState,
  wordsWithMeanings: Map[String, String],
  pageChan:          Channel[Boolean]
): RIO[Any, Unit] =
  Using(SessionDocumentWriter(docPath)): doc ⇒
    doc add HeaderParagraph(titleInput)
    doc add (WorksheetTable(tableState) setMarginTop 25)
    doc add (MeaningsTable(tableState, wordsWithMeanings) setMarginTop 25)
    doc add AreaBreak(AreaBreakType.NEXT_PAGE)
    doc add HeaderParagraph("Answers")
    doc add (AnswerTable(tableState) setMarginTop 25)

  (pageChan send true) mapError:
    err ⇒ Exception(err.toString)
