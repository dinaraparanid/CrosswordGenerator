package data.generation.population

/**
 * A single word in crossword's table
 *
 * @param word        The word itself
 * @param startRow    zero-based row index where the word starts
 * @param startColumn zero-based column index where the word starts
 * @param layout      layout of the word: 0 - horizontal, 1 - vertical
 */

case class WordState(
  word: String,
  startRow: Int,
  startColumn: Int,
  layout: Layout
)
