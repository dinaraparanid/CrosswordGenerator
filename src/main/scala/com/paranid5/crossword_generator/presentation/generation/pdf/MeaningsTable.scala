package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.{Cell, Table}
import com.itextpdf.layout.properties.HorizontalAlignment

import com.paranid5.crossword_generator.data.generation.population.{Layout, TableState, WordState}
import com.paranid5.crossword_generator.domain.utils.tailOrNil

import scala.annotation.tailrec

def MeaningsTable(
  tableState:        TableState,
  wordsWithMeanings: Map[String, String]
): Table =
  val pdfTable = Table(2)
    .setHorizontalAlignment(HorizontalAlignment.CENTER)
    .setFontSize(12)
    .addHeaderCell(headerCell("Horizontal"))
    .addHeaderCell(headerCell("Vertical"))

  val TableState(_, wordStates)        = tableState

  val (horizontalWords, verticalWords) = wordStates
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
            meaningsCell(head, wordsWithMeanings)
        )

  putCells()

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
      return
        if turn % 2 == 0 then res
        else Option.empty :: res

    if turn % 2 == 0 then
      impl(hw.tailOrNil, vw, turn + 1, hw.headOption :: res)
    else
      impl(hw, vw.tailOrNil, turn + 1, vw.headOption :: res)

  impl().reverse

private def headerCell(text: String): Cell =
  Cell() add HeaderParagraph(text = text, fontSize = 14)

private def meaningsCell(
  wordState:         Option[(WordState, Int)],
  wordsWithMeanings: Map[String, String]
): Cell =
  wordState match
    case None                 ⇒ Cell()
    case Some((value, index)) ⇒ TextCell:
      s"${index + 1}. ${wordsWithMeanings(value.word).capitalize}"
