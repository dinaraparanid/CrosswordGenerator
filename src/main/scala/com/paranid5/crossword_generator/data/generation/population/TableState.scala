package com.paranid5.crossword_generator.data.generation.population

import cats.Show
import cats.implicits.*

/** Wrapper for a table of characters */

type Table = Array[Array[Char]]

/**
 * The state of a crossword's table
 *
 * @param table table with placed words
 * @param words words' states, placed in the table
 */

case class TableState(table: Table, words: List[WordState])

object TableState:
  given showTableState: Show[TableState] = Show.show: t ⇒
    showTable(t.table)

def showTable(table: Table): String =
  table map (_ mkString " ") mkString "\n"