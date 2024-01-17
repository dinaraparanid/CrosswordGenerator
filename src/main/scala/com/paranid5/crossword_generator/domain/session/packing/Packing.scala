package com.paranid5.crossword_generator.domain.session.packing

import com.paranid5.crossword_generator.data.generation.population.{TableState, WordState}
import com.paranid5.crossword_generator.domain.generation.population.{emptyTable, putWord}

import zio.UIO

import scala.annotation.tailrec

/**
 * Packs the given [[tableState]],
 * removing padding from 4 sides,
 * transforming the table state
 * to the least possible squared table.
 *
 * @param tableState table state to pack
 * @return packed table, where all words are
 *         generally placed in the same way,
 *         but padding is removed
 */

def packed(tableState: TableState): UIO[TableState] =
  for
    borders ← minTableSizePar(tableState.table)
    (top, bottom, left, right) = borders

    width     = right - left + 1
    height    = bottom - top + 1
    tableSize = math.max(width, height)
  yield packed(tableState, tableSize, top, left)

/**
 * Packs the given [[tableState]],
 * removing padding from 4 sides,
 * transforming the table state
 * to the least possible squared table.
 *
 * @param tableState table state to pack
 * @param tableSize  reduced table size
 * @param top        first row where content is not empty
 * @param left       first column where content is not empty
 * @return packed table, where all words are
 *         generally placed in the same way,
 *         but padding is removed
 * @see [[minTableSizePar]]
 */

private def packed(
  tableState: TableState,
  tableSize:  Int,
  top:        Int,
  left:       Int
): TableState =
  val table = emptyTable(tableSize)
  val words = packedWordStates(tableState.words, top, left)

  @tailrec
  def putWords(
    ws:  List[WordState] = words,
    hws: List[WordState] = Nil,
    vws: List[WordState] = Nil
  ): Unit =
    ws match
      case Nil          ⇒
      case head :: next ⇒
        val (hw, vw) = putWord(head, table, hws, vws)
        putWords(next, hw, vw)

  putWords()
  TableState(table, words)

/**
 * Moves given [[wordStates]] to the up-left with the given offsets
 *
 * @param wordStates word states to move
 * @param top        up offset (startRow -= top)
 * @param left       left offset (startColumn -= left)
 * @return list with moved word states
 */

private def packedWordStates(
  wordStates: List[WordState],
  top:        Int,
  left:       Int
): List[WordState] =
  wordStates map:
    case WordState(word, startRow, startColumn, layout) ⇒
      WordState(word, startRow - top, startColumn - left, layout)