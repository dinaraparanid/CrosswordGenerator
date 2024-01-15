package domain.generation.crossover

import data.generation.population.{Coords, CoordsWithLayout, Layout, Table, WordState}
import domain.generation.selection.{canPutWordHorizontal, canPutWordVertical}

/**
 * Attempts to place a word state in the table
 * while trying to cross with other words to improve fitness
 *
 * @param wordState       the word state to place
 * @param table           the table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return the same word state, if placement is successful
 */

def tryPutCrossing(
  wordState:       WordState,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): Option[WordState] =
  val word = wordState.word
  tryCrossingStartCoords(word, table, horizontalWords, verticalWords) map:
    case CoordsWithLayout(row, column, layout) ⇒
      WordState(word, row, column, layout)

/**
 * Attempts to find starting coordinates for a word on the table,
 * that may produce an intersection with other words,
 * considering both horizontal and vertical placement
 *
 * @param word            the word to place
 * @param table           the table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return the same word state, if placement is successful
 */

private def tryCrossingStartCoords(
  word:            String,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): Option[CoordsWithLayout] =
  List(
    tryCrossingStartCoordsHorizontal(word, table, horizontalWords, verticalWords),
    tryCrossingStartCoordsVertical(word, table, horizontalWords, verticalWords)
  )
    .flatten
    .headOption

/**
 * Attempts to find starting coordinates for a word on the table,
 * that may produce an intersection with other words,
 * considering only horizontal placement
 *
 * @param word            the word to place
 * @param table           the table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return the same word state, if placement is successful
 * @see [[tryCrossingStartCoords]]
 */

private def tryCrossingStartCoordsHorizontal(
  word:            String,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): Option[CoordsWithLayout] =
  @inline
  def tryPutWord(row: Int, column: Int): Boolean =
    canPutWordHorizontal(word, row, column, table, horizontalWords)

  possibleConnections(word, verticalWords)
    .flatMap(possibleStartCoordsHorizontal(word, _))
    .find { case CoordsWithLayout(row, column, _) ⇒ tryPutWord(row, column) }

/**
 * Attempts to find starting coordinates for a word on the table,
 * that may produce an intersection with other words,
 * considering only vertical placement
 *
 * @param word            the word to place
 * @param table           the table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return the same word state, if placement is successful
 * @see [[tryCrossingStartCoords]]
 */

private def tryCrossingStartCoordsVertical(
  word:            String,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): Option[CoordsWithLayout] = {
  @inline
  def tryPutWord(row: Int, column: Int): Boolean =
    canPutWordVertical(word, row, column, table, verticalWords)

  possibleConnections(word, horizontalWords)
    .flatMap(possibleStartCoordsVertical(word, _))
    .find { case CoordsWithLayout(row, column, _) ⇒ tryPutWord(row, column) }
}

/**
 * Calculates the starting coordinates
 * for a horizontal word placement
 * based on a given intersection point
 *
 * @param word       the word to place
 * @param connection the connection point coordinates
 * @return obtained starting coordinates with a horizontal layout,
 *         if word can be placed with an intersection.
 * @see [[tryCrossingStartCoordsHorizontal]]
 */

private def possibleStartCoordsHorizontal(
  word:       String,
  connection: Coords
): Option[CoordsWithLayout] =
  val Coords(row, column, letter) = connection
  val index = word indexOf letter

  if column < index then Option.empty
  else Option(CoordsWithLayout(row, column - index, Layout.HORIZONTAL))

/**
 * Calculates the starting coordinates
 * for a vertical word placement
 * based on a given intersection point
 *
 * @param word       the word to place
 * @param connection the connection point coordinates
 * @return obtained starting coordinates with a vertical layout,
 *         if word can be placed with an intersection.
 * @see [[tryCrossingStartCoordsVertical]]
 */

private def possibleStartCoordsVertical(
  word:       String,
  connection: Coords
): Option[CoordsWithLayout] =
  val Coords(row, column, letter) = connection
  val index = word indexOf letter

  if row < index then Option.empty
  else Option(CoordsWithLayout(row - index, column, Layout.VERTICAL))
