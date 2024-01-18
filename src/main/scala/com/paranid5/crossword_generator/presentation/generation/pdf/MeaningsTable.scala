package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.{Cell, Paragraph, Table}
import com.itextpdf.layout.properties.HorizontalAlignment

import com.paranid5.crossword_generator.data.generation.population.{Layout, TableState, WordState}
import com.paranid5.crossword_generator.domain.utils.tailOrNil

import scala.annotation.tailrec

/**
 * Composes two columned table with words meanings.
 * First column represents horizontally placed words' meanings,
 * second column contains vertically placed words' meanings
 *
 * @param tableState        generated crossword table
 * @param wordsWithMeanings map of words with their meanings
 * @return pdf [[Table]] with meanings
 */

def MeaningsTable(
  tableState:        TableState,
  wordsWithMeanings: Map[String, String]
): Table =
  val pdfTable                  = initialPDFTable
  val TableState(_, wordStates) = tableState

  val (horizontalWords, verticalWords) =
    wordStates
      .zipWithIndex
      .partition(_._1.layout == Layout.HORIZONTAL)

  val tableWords = tableWordStates(horizontalWords, verticalWords)

  @tailrec
  def putCells(
    words:  List[Option[(WordState, Int)]] = tableWords,
    pdfTab: Table = pdfTable
  ): Table =
    words match
      case Nil          ⇒ pdfTab
      case head :: next ⇒
        putCells(
          words = next,
          pdfTab = pdfTab addCell
            MeaningsCell(head, wordsWithMeanings)
        )

  putCells()

/**
 * Empty two-columned pdf table
 * with headers 'Horizontal' and 'Vertical'
 */

@inline
private def initialPDFTable: Table =
  Table(2)
    .setHorizontalAlignment(HorizontalAlignment.CENTER)
    .setFontSize(12)
    .addHeaderCell(headerCell("Horizontal"))
    .addHeaderCell(headerCell("Vertical"))

/**
 * Paired words states from both [[horizontalWords]] and [[verticalWords]].
 * Such pairs fill two-columned table with corresponding columns.
 * If h/v lists have different sizes, none is placed to the list
 * when pair cannot be formed
 *
 * @param horizontalWords horizontally placed words (1-st column)
 * @param verticalWords   vertically placed words (2-nd column)
 * @return list of words h/v words one after another
 */

private def tableWordStates(
  horizontalWords: List[(WordState, Int)],
  verticalWords:   List[(WordState, Int)]
): List[Option[(WordState, Int)]] =
  @tailrec
  def impl(
    hw:   List[(WordState, Int)] = horizontalWords,
    vw:   List[(WordState, Int)] = verticalWords,
    turn: Int = 0,
    res:  List[Option[(WordState, Int)]] = Nil
  ): List[Option[(WordState, Int)]] =
    if hw.isEmpty && vw.isEmpty then
        if turn % 2 == 0 then res
        else Option.empty :: res
    else if turn % 2 == 0 then
      impl(hw.tailOrNil, vw, turn + 1, hw.headOption :: res)
    else
      impl(hw, vw.tailOrNil, turn + 1, vw.headOption :: res)

  impl().reverse

/**
 * Cell with HeaderParagraph and font = 14
 * @param text header text
 * @return [[Cell]] with [[HeaderParagraph]]
 */

@inline
private def headerCell(text: String): Cell =
  Cell() add HeaderParagraph(text = text, fontSize = 14)

/**
 * Cell for the [[MeaningsTable]]
 *
 * @param wordState         word with its index or none
 *                          if there is nothing to put
 * @param wordsWithMeanings map of words with their meanings
 * @return either "$index. $meaning" cell or empty cell
 */

@inline
private def MeaningsCell(
  wordState:         Option[(WordState, Int)],
  wordsWithMeanings: Map[String, String]
): Cell =
  wordState match
    case None                 ⇒ Cell()
    case Some((value, index)) ⇒ MeaningsTextCell:
      s"${index + 1}. ${wordsWithMeanings(value.word).capitalize}"

/**
 * Texted cell for the [[MeaningsTable]]
 * with additional padding to not
 * go beyond table's borders
 *
 * @param text text for the cell
 * @return [[Cell]] with given text
 */

@inline
private def MeaningsTextCell(text: String): Cell =
  Cell()
    .setPaddingLeft(5F)
    .setPaddingRight(50F)
    .add(Paragraph(text))
