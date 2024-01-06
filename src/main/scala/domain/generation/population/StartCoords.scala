package domain.generation.population

import domain.generation.generator
import data.generation.population.{Coords, Layout}

import scala.util.Random

/**
 * Generates the starting coordinates for placing a word in the table
 * based on the given layout (0 - horizontal, 1 - vertical).
 * Generated coordinates guaranteed to have no overlap with both neighbours and table's borders
 *
 * @param word   word to be placed in the table
 * @param layout layout of the word (0 - horizontal, 1 - vertical)
 * @return tuple of starting row and column
 */

def startCoords(word: String, tableSize: Int, layout: Layout): Coords =
  layout match
    case Layout.HORIZONTAL ⇒ startCoordsHorizontal(word, tableSize)
    case Layout.VERTICAL ⇒ startCoordsVertical(word, tableSize)

/**
 * Generates the starting coordinates for placing a word in the table in horizontal position.
 * Generated coordinates guaranteed to have no overlap with both neighbours and table's borders
 *
 * @param word   word to be placed in the table
 * @param random random generator
 * @return tuple of starting row and column
 */

private def startCoordsHorizontal(word: String, tableSize: Int)(using random: Random): Coords =
  val startRow = random nextInt tableSize
  val startColumn = random nextInt (tableSize - word.length + 1)
  Coords(startRow, startColumn, EmptyLetter)

/**
 * Generates the starting coordinates for placing a word in the table in vertical position.
 * Generated coordinates guaranteed to have no overlap with both neighbours and table's borders
 *
 * @param word   word to be placed in the table
 * @param random random generator
 * @return tuple of starting row and column
 */

private def startCoordsVertical(word: String, tableSize: Int)(using random: Random): Coords =
  val startRow = random nextInt (tableSize - word.length + 1)
  val startColumn = random nextInt tableSize
  Coords(startRow, startColumn, EmptyLetter)