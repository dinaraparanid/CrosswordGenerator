package com.paranid5.crossword_generator.presentation.generation.input

import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.properties.AreaBreakType

import com.paranid5.crossword_generator.data.app.SessionChannel
import com.paranid5.crossword_generator.data.generation.population.TableState
import com.paranid5.crossword_generator.data.storage.*
import com.paranid5.crossword_generator.data.utils.toRIO

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

/**
 * Defines a button that starts crossword generation
 * from the given words, if they are correct
 */

def GenerateButton(): URIO[StoragePreferences & SessionChannel, JButton] =
  val button = new JButton("Generate"):
    putClientProperty("JButton.buttonType", "roundRect")

  val runtime = Runtime.default

  /**
   * Updates the button's enabled state
   * and the on click callback,
   * based on the inputs from user
   *
   * @param isCrosswordCorrect is words input correct
   * @param titleInput         entered title
   * @param wordsInput         entered words with their meanings
   * @param sessionDoc         path to the session pdf doc
   * @param pageChan           page updates channel
   */

  @inline
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

/**
 * Generates a crossword table
 * based on the inputted words
 *
 * @param wordsInput entered words with meanings
 * @return generated crossword as the [[TableState]],
 *         map of words with their meanings
 */

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

/**
 * Calculates the minimum table size
 * required to accommodate the inputted words.
 *
 * @param words words to place in the table
 * @return max(maxWordLength, wordsSize) * 2
 */

private def requiredTableSize(words: Iterable[String]): Int =
  val maxLength = words.map(_.length).max
  val size = words.size
  math.max(maxLength, size) * 2

/**
 * Adds the crossword's title, worksheet
 * and the answer key to the session document.
 * Then sends broadcast to update pdf document
 *
 * @param docPath           session pdf doc path
 * @param titleInput        entered title
 * @param tableState        generated crossword table
 * @param wordsWithMeanings entered words with their meanings
 * @param pageChan          update pdf doc channel
 * @return RIO that completes when update message is sent
 */

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
