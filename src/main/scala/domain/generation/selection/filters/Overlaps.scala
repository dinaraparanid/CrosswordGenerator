package domain.generation.selection.filters

import data.generation.population.{Layout, Table}
import domain.generation.population.EmptyLetter

/**
 * Checks whether a horizontal word state
 * have an overlap that is not crossing
 *
 * @param word        word to check
 * @param startRow    starting row coordinate for the word
 * @param startColumn starting column coordinate for the word
 * @param table       current table with characters
 * @return true if word's placement will result in overlap
 */

def hasOverlapsHorizontal(
  word:        String,
  startRow:    Int,
  startColumn: Int,
  table:       Table
): Boolean = (startColumn until startColumn + word.length)
  .map(table(startRow))
  .zip(word)
  .exists { case (tab, w) ⇒ tab != EmptyLetter && tab != w }

/**
 * Checks whether a vertical word state
 * have an overlap that is not crossing
 *
 * @param word        word to check
 * @param startRow    starting row coordinate for the word
 * @param startColumn starting column coordinate for the word
 * @param table       current table with characters
 * @return true if word's placement will result in overlap
 */

def hasOverlapsVertical(
  word:        String,
  startRow:    Int,
  startColumn: Int,
  table:       Table
): Boolean = (startRow until startRow + word.length)
  .map { table(_)(startColumn) }
  .zip { word }
  .exists { case (tab, w) ⇒ tab != EmptyLetter && tab != w }