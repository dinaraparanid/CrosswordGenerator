package com.paranid5.crossword_generator.domain.session.packing

import com.paranid5.crossword_generator.data.generation.population.Table
import com.paranid5.crossword_generator.domain.generation.population.EmptyLetter

import zio.{UIO, ZIO}

/**
 * Searches for the minimum table size
 * of the given [[table]] in parallel
 *
 * @param table the table itself
 * @return the first row with content (top),
 *         the last row with content (bottom),
 *         the first column with content (left),
 *         the last column with the content (right)
 */

private def minTableSizePar(table: Table): UIO[(Int, Int, Int, Int)] =
  for
    top    ← ZIO.succeedBlocking(topBorder(table)).fork
    bottom ← ZIO.succeedBlocking(bottomBorder(table)).fork
    left   ← ZIO.succeedBlocking(leftBorder(table)).fork
    right  ← ZIO.succeedBlocking(rightBorder(table)).fork
    res    ← (top <*> bottom <*> left <*> right).join
  yield res

/**
 * Searches for the first row with
 * at least one letter in given table
 *
 * @param table the table itself
 * @return the first row's index where there
 *         exists a cell that is not [[EmptyLetter]]
 */

private def topBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i1, i2) },
    border = { case (row, _) ⇒ row }
  )

/**
 * Searches for the last row with
 * at least one letter in given table
 *
 * @param table the table itself
 * @return the last row's index where there
 *         exists a cell that is not [[EmptyLetter]]
 */

private def bottomBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices.reverse,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i1, i2) },
    border = { case (row, _) ⇒ row }
  )

/**
 * Searches for the first column with
 * at least one letter in given table
 *
 * @param table the table itself
 * @return the first column's index where there
 *         exists a cell that is not [[EmptyLetter]]
 */

private def leftBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i2, i1) },
    border = { case (_, column) ⇒ column }
  )

/**
 * Searches for the last column with
 * at least one letter in given table
 *
 * @param table the table itself
 * @return the last column's index where there
 *         exists a cell that is not [[EmptyLetter]]
 */

private def rightBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices.reverse,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i2, i1) },
    border = { case (_, column) ⇒ column }
  )

/**
 * Searches for the first matching border
 * with at least one letter in given table
 *
 * @param table       the table itself
 * @param firstRange  first traversal index range
 * @param secondRange second traversal index range
 * @param indices     row and column indices mapper,
 *                    based on the [[firstRange]] and [[secondRange]]
 * @param border      required border from row and column
 * @return the first matching border's index where there
 *         exists a cell that is not [[EmptyLetter]]
 */

@inline
private def scanBorders(
  table:       Table,
  firstRange:  Range,
  secondRange: Range,
  indices:     (Int, Int) ⇒ (Int, Int),
  border:      (Int, Int) ⇒ Int
): Int =
  val res = for
    i1 ← firstRange
    i2 ← secondRange

    (row, column) = indices(i1, i2)
    value = table(row)(column)
    res = border(row, column) if value != EmptyLetter
  yield res

  res.head
