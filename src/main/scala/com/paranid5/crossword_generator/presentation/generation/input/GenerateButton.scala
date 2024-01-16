package com.paranid5.crossword_generator.presentation.generation.input

import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.properties.AreaBreakType

import com.paranid5.crossword_generator.data.app.SessionBroadcast
import com.paranid5.crossword_generator.data.generation.population.TableState
import com.paranid5.crossword_generator.data.storage.*

import com.paranid5.crossword_generator.domain.generation.generation
import com.paranid5.crossword_generator.domain.session.packing.packed
import com.paranid5.crossword_generator.domain.session.{SessionDocumentWriter, parsedWordsWithMeanings}

import com.paranid5.crossword_generator.presentation.generation.pdf.*
import com.paranid5.crossword_generator.presentation.ui.utils.{combine, removeActionListeners}
import com.paranid5.crossword_generator.presentation.updatePageChannel

import zio.channel.Channel
import zio.{RIO, Runtime, UIO, URIO, Unsafe, ZIO}

import javax.swing.JButton

import scala.util.Using

def GenerateButton(): URIO[StoragePreferences & SessionBroadcast, JButton] =
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
    isCorrectStream ← isInputCorrectStream
    titleStream     ← titleInputStream
    wordsStream     ← wordsInputStream
    docStream       ← sessionDocPathStream
    pageChan        ← updatePageChannel()

    _ ← combine(
      isCorrectStream,
      titleStream,
      wordsStream,
      docStream
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

  (pageChan send true).toRIO
