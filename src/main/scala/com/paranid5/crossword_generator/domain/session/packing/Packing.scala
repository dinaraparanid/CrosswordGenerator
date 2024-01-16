package com.paranid5.crossword_generator.domain.session.packing

import com.paranid5.crossword_generator.data.generation.population.{TableState, WordState}
import com.paranid5.crossword_generator.domain.generation.population.{emptyTable, putWord}

import zio.UIO

import scala.annotation.tailrec

def packed(tableState: TableState): UIO[TableState] =
  for
    borders ← minTableSizePar(tableState.table)
    (top, bottom, left, right) = borders

    width     = right - left + 1
    height    = bottom - top + 1
    tableSize = math.max(width, height)
  yield packed(tableState, tableSize, top, left)

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

private def packedWordStates(
  wordStates: List[WordState],
  top:        Int,
  left:       Int
): List[WordState] =
  wordStates map:
    case WordState(word, startRow, startColumn, layout) ⇒
      WordState(word, startRow - top, startColumn - left, layout)