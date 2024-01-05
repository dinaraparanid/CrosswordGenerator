package domain.generation.selection

import data.generation.population.{Coords, Layout, WordState}

/**
 * Creates a map associating each word
 * with its coordinates for every symbol
 *
 * @param wordStates list of word states to process
 * @return map where each key represents a word state
 *         and the value as every symbol's coordinates
 */

private def wordsCoords(wordStates: List[WordState]): Map[WordState, Set[Coords]] =
	wordStates.map { word ⇒ (word, wordCoords(word)) }.toMap

/**
 * Retrieves the coordinates of a word for every symbol based on its layout
 *
 * @param wordState the word state to process
 * @return A set of coordinates representing the positions
 *         occupied by every symbol of the word
 */

private def wordCoords(wordState: WordState): Set[Coords] =
	wordState.layout match
		case Layout.HORIZONTAL ⇒ wordCoordsHorizontal(wordState)
		case Layout.VERTICAL ⇒ wordCoordsVertical(wordState)

/**
 * Retrieves the coordinates of a horizontally-placed word for every symbol
 *
 * @param wordState the word state to process
 * @return A set of coordinates representing the positions
 *         occupied by every symbol of the word
 */

private def wordCoordsHorizontal(wordState: WordState): Set[Coords] =
	(wordState.startColumn until wordState.startColumn + wordState.word.length)
		.map { c ⇒ Coords(wordState.startRow, c, wordState.word(c - wordState.startColumn)) }
		.toSet

/**
 * Retrieves the coordinates of a vertically-placed word for every symbol
 *
 * @param wordState the word state to process
 * @return A set of coordinates representing the positions
 *         occupied by every symbol of the word
 */

private def wordCoordsVertical(wordState: WordState): Set[Coords] =
	(wordState.startRow until wordState.startRow + wordState.word.length)
		.map { r ⇒ Coords(r, wordState.startColumn, wordState.word(r - wordState.startRow)) }
		.toSet

/**
 * Extracts the set of column coordinates
 * occupied by a given word state in the table
 *
 * @param wordState word from which to extract the columns
 * @return set of column indices occupied by the word
 */

private def columnSet(wordState: WordState): Set[Int] =
	wordCoords(wordState) map (_.column)

/**
 * Extracts the set of rows coordinates
 * occupied by a given word state in the table
 *
 * @param wordState word from which to extract the rows
 * @return set of row indices occupied by the word
 */

private def rowSet(wordState: WordState): Set[Int] =
	wordCoords(wordState) map (_.row)