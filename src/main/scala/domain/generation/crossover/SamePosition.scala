package domain.generation.crossover

import data.generation.population.{Table, WordState}
import domain.generation.selection.canPutWord

/**
 * Attempts to place a word state in the table with [[canPutWord]]
 * while maintaining the same position as in the parent's table if possible
 *
 * @param wordState       the word state to place
 * @param table           the table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return the same word state, if placement is successful
 */

private def tryPutWithSamePosition(
  wordState:       WordState,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): Option[WordState] =
  val canPut = canPutWord(wordState, table, horizontalWords, verticalWords)
  if canPut then Option(wordState) else Option.empty