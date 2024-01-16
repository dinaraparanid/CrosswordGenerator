package com.paranid5.crossword_generator.domain.generation.selection.filters

import com.paranid5.crossword_generator.data.generation.population.{Layout, Table, WordState}
import com.paranid5.crossword_generator.domain.generation.population.EmptyLetter

/**
 * Checks if the word is adjacent with others, but not crossed.
 * Example: "b_e_x_a_m" - b does not belong to the word "exam"
 *
 * @param wordState word state to be placed onto the table
 * @param table     table in which to place the word
 * @return true if the word is followed by other words
 */

def followed(wordState: WordState, table: Table): Boolean =
  followed(
    word = wordState.word,
    startRow = wordState.startRow,
    startColumn = wordState.startColumn,
    layout = wordState.layout,
    table = table
  )

/**
 * Checks if the word is adjacent with others, but not crossed.
 * Example: "b_e_x_a_m" - b does not belong to the word "exam"
 *
 * @param word        word to be placed onto the table
 * @param startRow    starting row coordinate for the word
 * @param startColumn starting column coordinate for the word
 * @param layout      layout of the word
 * @param table       table in which to place the word
 * @return true if the word is followed by other words
 */

def followed(
  word:        String,
  startRow:    Int,
  startColumn: Int,
  layout:      Layout,
  table:       Table
): Boolean =
  layout match
    case Layout.HORIZONTAL ⇒ followedHorizontal(word, startRow, startColumn, table)
    case Layout.VERTICAL ⇒ followedVertical(word, startRow, startColumn, table)

/**
 * Checks if the horizontal word is adjacent with others, but not crossed.
 * Example: "b_e_x_a_m" - b does not belong to the word "exam"
 *
 * @param word        word to be placed in the table
 * @param startRow    starting row coordinate for the word
 * @param startColumn starting column coordinate for the word
 * @param table       table in which to place the word
 * @return true if the word is followed by other words
 */

def followedHorizontal(
  word:        String,
  startRow:    Int,
  startColumn: Int,
  table:       Table
): Boolean =
  if startColumn > 0 then
    if table(startRow)(startColumn - 1) != EmptyLetter then
      return true

  if startColumn + word.length + 1 < table.length then
    return table(startRow)(startColumn + word.length) != EmptyLetter

  false

/**
 * Checks if the vertical word is adjacent with others, but not crossed.
 * Example: "b_e_x_a_m" - b does not belong to the word "exam"
 *
 * @param word        word to be placed in the table
 * @param startRow    starting row coordinate for the word
 * @param startColumn starting column coordinate for the word
 * @param table       table in which to place the word
 * @return true if the word is followed by other words
 */

def followedVertical(
  word:        String,
  startRow:    Int,
  startColumn: Int,
  table:       Table
): Boolean =
  if startRow > 0 then
    if table(startRow - 1)(startColumn) != EmptyLetter then
      return true

  if startRow + word.length + 1 < table.length then
    return table(startRow + word.length)(startColumn) != EmptyLetter

  false
