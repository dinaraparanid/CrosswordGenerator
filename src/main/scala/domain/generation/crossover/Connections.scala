package domain.generation.crossover

import data.generation.population.{Coords, Layout, WordState}

type LetterMap = Map[Char, Seq[Coords]]

/**
 * Generates a stream of possible intersection points
 * for a word based on the coordinates of existing word states.
 * Computes coordinates of every word using [[letterMap]],
 * then filters coordinates of characters that are present in word,
 * using [[filterLetterMap]]
 *
 * @param word          the word that has to be intersected
 * @param directedWords the list of directed word states (either horizontal or vertical)
 * @return A stream of `Coords` objects representing the possible connection points.
 */

private def possibleConnections(
  word:          String,
  directedWords: List[WordState]
): List[Coords] =
  directedWords map letterMap flatMap filterLetterMap(word)

/**
 * Filters coordinates of characters in the map that are present in word
 *
 * @param word      word to check for connection points
 * @param letterMap map associating each letter with a list of corresponding coordinates
 * @return stream representing the potential connection points
 * @see [[possibleConnections]]
 */

private def filterLetterMap(word: String)(letterMap: LetterMap): Seq[Coords] =
  word.flatMap(letterMap.get).flatten

/**
 * Produces the coordinate map for a word,
 * where the key is the character
 * and the value is the list of coordinates,
 * where the symbol is located in the table
 *
 * @param wordState the word state to process
 * @return map associating each character in the word
 *         with a list of coordinates in the table
 */

private def letterMap(wordState: WordState): LetterMap =
  wordState.layout match
    case Layout.HORIZONTAL ⇒ letterMapHorizontal(wordState)
    case Layout.VERTICAL ⇒ letterMapVertical(wordState)

/**
 * Produces the coordinate map for a horizontal word,
 * where the key is the character
 * and the value is the list of coordinates,
 * where the symbol is located in the table
 *
 * @param wordState the word state to process
 * @return map associating each character in the word
 *         with a list of coordinates in the table
 */

private def letterMapHorizontal(wordState: WordState): LetterMap =
  val WordState(word, row, column, _) = wordState
  letterMap(column, word) { (c, letter) ⇒ Coords(row, c, letter) }

/**
 * Produces the coordinate map for a vertical word,
 * where the key is the character
 * and the value is the list of coordinates,
 * where the symbol is located in the table
 *
 * @param wordState the word state to process
 * @return map associating each character in the word
 *         with a list of coordinates in the table
 */

private def letterMapVertical(wordState: WordState): LetterMap =
  val WordState(word, row, column, _) = wordState
  letterMap(row, word) { (r, letter) ⇒ Coords(r, column, letter) }

/**
 * Produces the coordinate map for a word,
 * where the key is the character
 * and the value is the list of coordinates,
 * where the symbol is located in the table
 *
 * @param start start coordinate
 * @param word the word to process
 * @param f maps current coordinate and
 *          the corresponding letter to the [[Coords]]
 * @return map associating each character in the word
 *         with a list of coordinates in the table
 */

@inline
private def letterMap(start: Int, word: String)(f: (Int, Char) ⇒ Coords): LetterMap =
  (start until start + word.length)
    .zip(word)
    .map { case (col, letter) ⇒ f(col, letter) }
    .groupMap(_.letter)(identity)
