package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.{Cell, Paragraph, Table}
import com.itextpdf.layout.properties.{HorizontalAlignment, TextAlignment, UnitValue}

import com.paranid5.crossword_generator.data.generation.population.{Layout, TableState, WordState, Table as CrosswordTable}
import com.paranid5.crossword_generator.domain.generation.population.EmptyLetter

import scala.annotation.tailrec

private val DrainedCell = '*'

private type WordsStarts = Map[(Int, Int), (Layout, Int)]

private def CrosswordTable(
  tableState: TableState,
  textAlignment: TextAlignment = TextAlignment.CENTER,
  fontSize: Int = 12
)(letterCell: Char ⇒ String): Table =
  val pdfTable = Table(tableState.table.length)
    .setBorder(Border.NO_BORDER)
    .setHorizontalAlignment(HorizontalAlignment.CENTER)
    .setTextAlignment(textAlignment)
    .setFontSize(fontSize)
    .setBold()

  val TableState(tab, wordStates) = tableState
  val table                       = tab map (_.clone)
  val wordsStarts                 = enumeratedWordStarts(wordStates)

  val cellList = for
    row  ← table.indices
    col  ← table.indices
    cell ← crosswordCell(row, col, table, wordsStarts)(letterCell)
      .map(_ setMinWidth requiredSize setMinHeight requiredSize)
  yield cell

  @tailrec
  def putCells(
    cells:  List[Cell] = cellList.toList,
    pdfTab: Table = pdfTable
  ): Table =
    cells match
      case Nil          ⇒ pdfTab
      case head :: next ⇒
        putCells(next, pdfTab addCell head)

  putCells()

private def enumeratedWordStarts(wordStates: List[WordState]): WordsStarts =
  wordStates
    .zipWithIndex
    .map:
      case (WordState(_, row, col, layout), ind) ⇒
        ((row, col), (layout, ind))
    .toMap

private def crosswordCell(
  row:         Int,
  column:      Int,
  table:       CrosswordTable,
  wordsStarts: WordsStarts
)(letterCell: Char ⇒ String): Option[Cell] =
  table(row)(column) match
    case DrainedCell ⇒
      Option.empty

    case EmptyLetter ⇒
      Option(fatCell(table, row, column))

    case char ⇒
      wordsStarts get (row, column) match
        case Some((_, index)) ⇒
          Option(Cell() add Paragraph(s"${index + 1}.${letterCell(char)}"))

        case None ⇒
          Option(Cell() add Paragraph(letterCell(char)))

private def fatCell(
  table:       CrosswordTable,
  startRow:    Int,
  startColumn: Int
): Cell =
  @tailrec
  def freeCellsInRow(
    row:       Int,
    curColumn: Int = startColumn,
    count:     Int = 0
  ): Int =
    if curColumn >= table.length then
      return count

    table(row)(curColumn) match
      case EmptyLetter ⇒ freeCellsInRow(row, curColumn + 1, count + 1)
      case _           ⇒ count

  @tailrec
  def freeRowsWithSameWidth(
    width:  Int,
    curRow: Int = startRow,
    count:  Int = 0
  ): Int =
    if curRow >= table.length then
      return count

    if freeCellsInRow(row = curRow) >= width then
      freeRowsWithSameWidth(width, curRow + 1, count + 1)
    else count

  val columns = freeCellsInRow(row = startRow)
  val rows    = freeRowsWithSameWidth(width = columns)

  for
    r ← startRow until startRow + rows
    c ← startColumn until startColumn + columns
  do table(r)(c) = DrainedCell

  Cell(rows, columns) setBorder Border.NO_BORDER

private def requiredSize: UnitValue =
  UnitValue createPointValue 25
