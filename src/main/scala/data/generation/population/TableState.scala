package data.generation.population

import cats.Show

import scala.util.Random

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
  given showTable: Show[TableState] = Show.show {
    _.table map { _ mkString " " } mkString "\n"
  }