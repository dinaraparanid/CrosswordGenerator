package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.{Cell, Paragraph, Table}
import com.itextpdf.layout.properties.{HorizontalAlignment, TextAlignment, UnitValue}

import com.paranid5.crossword_generator.data.generation.population.{Layout, TableState, WordState, Table as CrosswordTable}
import com.paranid5.crossword_generator.domain.generation.population.EmptyLetter

import scala.annotation.tailrec

private val DrainedCell = '*'

/**
 * Mapping between word starts
 * and their corresponding layout and index
 */

private type WordsStarts = Map[(Int, Int), (Layout, Int)]

/**
 * Creates a PDF table representation of the crossword table
 *
 * @param tableState    generated crossword table
 * @param textAlignment text alignment of the table cells
 * @param fontSize      font size of the table cells
 * @param letterCell    maps characters to their corresponding strings
 *                      to display in the table cells
 * @return The PDF table representation of the crossword puzzle
 */

private def CrosswordTable(
  tableState:    TableState,
  textAlignment: TextAlignment = TextAlignment.CENTER,
  fontSize:      Int = 12
)(letterCell: Char ⇒ String): Table =
  val pdfTable = initialPdfTable(
    tableLength = tableState.table.length,
    textAlignment = textAlignment,
    fontSize = fontSize
  )

  val TableState(tab, wordStates) = tableState
  val table                       = tab map (_.clone)
  val wordsStarts                 = enumeratedWordStarts(wordStates)
  val cellSize                    = requiredCellSize(tab.length)

  val cellList = for
    row  ← table.indices
    col  ← table.indices
    cell ← crosswordCell(row, col, table, wordsStarts)(letterCell)
      .map(_ setMinWidth cellSize setMinHeight cellSize)
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

/**
 * Composes an empty pdf squared table
 *
 * @param tableLength   required table size
 * @param textAlignment letter alignment in the table's cells
 * @param fontSize      letters' font size
 * @return squared pdf [[Table]] with the given configuration
 */

@inline
private def initialPdfTable(
  tableLength:   Int,
  textAlignment: TextAlignment = TextAlignment.CENTER,
  fontSize:      Int = 12
): Table =
  Table(tableLength)
    .setBorder(Border.NO_BORDER)
    .setHorizontalAlignment(HorizontalAlignment.CENTER)
    .setTextAlignment(textAlignment)
    .setFontSize(fontSize)
    .setBold()

/**
 * Extracts the mapping between word starts
 * with their corresponding layout
 * and index from the [[wordStates]] list
 *
 * @param wordStates The list of word states
 * @return A [[WordsStarts]] map
 */

@inline
private def enumeratedWordStarts(wordStates: List[WordState]): WordsStarts =
  wordStates
    .zipWithIndex
    .map:
      case (WordState(_, row, col, layout), ind) ⇒
        ((row, col), (layout, ind))
    .toMap

/**
 * Creates a [[Cell]] for a crossword table cell
 * based on its row, column, table, words starts, and letter cell mapper
 *
 * @param row         row index of the cell
 * @param column      column index of the cell
 * @param table       crossword letter table
 * @param wordsStarts [[WordsStarts]] map
 * @param letterCell  mapper between cell's letter and the resulting string
 * @return [[Cell]] or none if the position is already drained
 */

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

/**
 * Creates a fat [[Cell]] for a crossword table cell
 * that drains rectangular space of max width with no letters
 * based on its row, column, table, words starts, and letter cell mapper
 *
 * @param table       crossword letter table
 * @param startRow    start row from which draining is started
 * @param startColumn start column from which draining is started
 * @return fat [[Cell]] that occupied maximum free space
 */

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

/** Minimum cell size */

@inline
private def requiredCellSize(tableSize: Int): UnitValue =
  UnitValue createPointValue math.min(25F / tableSize * 18F, 25F)
