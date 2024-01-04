package domain.generation.selection

import data.generation.population.Layout.{HORIZONTAL, VERTICAL}
import data.generation.population.{Layout, Table, WordState}

/**
 * Checks if the word can be placed in the table
 * with the given starting coordinates and the layout.
 * Word's placement has to fit table's bounds and make no overlaps with neighbours.
 *
 * @param word            word to be placed in the table
 * @param startRow        starting row coordinate for the word
 * @param startColumn     starting column coordinate for the word
 * @param layout          layout of the word (0 - horizontal, 1 - vertical)
 * @param table           table in which to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return true if the word can be placed without overlaps
 */

def canPutWord(
	word: String,
	startRow: Int,
	startColumn: Int,
	layout: Layout,
	table: Table,
	horizontalWords: List[WordState],
	verticalWords: List[WordState]
): Boolean = layout match
	case Layout.HORIZONTAL ⇒ canPutWordHorizontal(
		word = word,
		startRow = startRow,
		startColumn = startColumn,
		table = table,
		horizontalWords = horizontalWords
	)

	case Layout.VERTICAL ⇒ canPutWordVertical(
		word = word,
		startRow = startRow,
		startColumn = startColumn,
		table = table,
		verticalWords = verticalWords
	)

/**
 * Checks if the word can be placed in the table
 * with the given starting coordinates and the layout.
 * Word's placement has to fit table's bounds and make no overlaps with neighbours.
 *
 * @param wordState       word with its starting coordinates and the layout
 * @param table           table in which to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return true if the word can be placed without overlaps
 */

def canPutWord(
	wordState: WordState,
	table: Table,
	horizontalWords: List[WordState],
	verticalWords: List[WordState]
): Boolean = canPutWord(
	word = wordState.word,
	startRow = wordState.startRow,
	startColumn = wordState.startColumn,
	layout = wordState.layout,
	table = table,
	horizontalWords = horizontalWords,
	verticalWords = verticalWords
)

/**
 * Checks if the word can be placed horizontally
 * in the table with the given starting coordinates.
 * Word's placement has to fit table's bounds and make no overlaps with neighbours.
 *
 * @param word            word to be placed in the table
 * @param startRow        starting row coordinate for the word
 * @param startColumn     starting column coordinate for the word
 * @param table           table in which to place the word
 * @param horizontalWords horizontal words placed in the table
 * @return true if the word can be placed without overlaps
 */

private def canPutWordHorizontal(
	word:            String,
	startRow:        Int,
	startColumn:     Int,
	table:           Table,
	horizontalWords: List[WordState],
): Boolean =
	if startColumn + word.length >= table.length then
		return false

	if followedHorizontal(word, startRow, startColumn, table) then
		return false

	if hasOverlapsHorizontal(word, startRow, startColumn, table) then
		return false

	val wordState = WordState(word, startRow, startColumn, layout = HORIZONTAL)

	horizontalWords forall { w ⇒
		!tooBigNeighbouringBorderHorizontal(wordState, w) &&
			!adjacentHorizontal(wordState, w)
	}

/**
 * Checks if the word can be placed vertically
 * in the table with the given starting coordinates.
 * Word's placement has to fit table's bounds and make no overlaps with neighbours.
 *
 * @param word          word to be placed in the table
 * @param startRow      starting row coordinate for the word
 * @param startColumn   starting column coordinate for the word
 * @param table         table in which to place the word
 * @param verticalWords vertical words placed in the table
 * @return true if the word can be placed without overlaps
 */

private def canPutWordVertical(
	word:          String,
	startRow:      Int,
	startColumn:   Int,
	table:         Table,
	verticalWords: List[WordState],
): Boolean = {
	if startRow + word.length >= table.length then
		return false

	if followedVertical(word, startRow, startColumn, table) then
		return false

	if hasOverlapsVertical(word, startRow, startColumn, table) then
		return false

	val wordState = WordState(word, startRow, startColumn, layout = VERTICAL)

	verticalWords forall { w ⇒
		!tooBigNeighbouringBorderVertical(wordState, w) &&
			!adjacentVertical(wordState, w)
	}
}