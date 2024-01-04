package domain.generation.selection

import data.generation.population.{Coords, Layout, WordState}

/**
 * Retrieves the coordinates of a word for every symbol based on its layout
 *
 * @param wordState the word state to process
 * @return A set of coordinates representing the positions
 *         occupied by every symbol of the word
 */

def wordCoords(wordState: WordState): Set[Coords] =
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
