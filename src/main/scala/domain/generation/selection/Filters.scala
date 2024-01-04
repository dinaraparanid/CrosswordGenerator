package domain.generation.selection

import data.generation.population.{Table, WordState}

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

private def followedHorizontal(
	word:        String,
	startRow:    Int,
	startColumn: Int,
	table:       Table
): Boolean =
	if startColumn > 0 then
		if table(startRow)(startColumn - 1) != 0 then
			return true

	if startColumn + word.length + 1 < table.length then
		return table(startRow)(startColumn + word.length) != 0

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

private def followedVertical(
	word:        String,
	startRow:    Int,
	startColumn: Int,
	table:       Table
): Boolean =
	if startRow > 0 then
		if table(startRow - 1)(startColumn) != 0 then
			return true

	if startRow + word.length + 1 < table.length then
		return table(startRow + word.length)(startColumn) != 0

	false

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

private def hasOverlapsHorizontal(
	word:        String,
	startRow:    Int,
	startColumn: Int,
	table:       Table
): Boolean = (startColumn until startColumn + word.length)
	.map(table(startRow))
	.zip(word)
	.exists { case (tab, w) ⇒ tab != 0 && tab != w }

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

private def hasOverlapsVertical(
	word:        String,
	startRow:    Int,
	startColumn: Int,
	table:       Table
): Boolean = (startRow until startRow + word.length)
	.map { table(_)(startColumn) }
	.zip { word }
	.exists { case (tab, w) ⇒ tab != 0 && tab != w }

/**
 * Checks whether two horizontal word states
 * have a neighboring border greater than 1,
 * indicating an overlap
 *
 * @param a first word state
 * @param b second word state
 * @return true if the neighboring border is too large.
 */

private def tooBigNeighbouringBorderHorizontal(
	a: WordState,
	b: WordState
): Boolean =
	if math.abs(a.startRow - b.startRow) != 1 then
		return false

	val aCoords = columnSet(a)
	val bCoords = columnSet(b)

	(aCoords & bCoords).sizeIs > 1

/**
 * Checks whether two vertical word states
 * have a neighboring border greater than 1,
 * indicating an overlap
 *
 * @param a first word state
 * @param b second word state
 * @return true if the neighboring border is too large.
 */

private def tooBigNeighbouringBorderVertical(
	a: WordState,
	b: WordState
): Boolean =
	if math.abs(a.startColumn - b.startColumn) != 1 then
		return false

	val aCoords = rowSet(a)
	val bCoords = rowSet(b)

	(aCoords & bCoords).sizeIs > 1

/**
 * Extracts the set of column coordinates
 * occupied by a given word state in the table
 *
 * @param wordState word from which to extract the columns
 * @return set of column indices occupied by the word
 */

private def columnSet(wordState: WordState): Set[Int] =
	wordCoords(wordState).map(_.column)

/**
 * Extracts the set of rows coordinates
 * occupied by a given word state in the table
 *
 * @param wordState word from which to extract the rows
 * @return set of row indices occupied by the word
 */

private def rowSet(wordState: WordState): Set[Int] =
	wordCoords(wordState).map(_.row)

/**
 * Checks whether two horizontal words are directly adjacent to each other
 *
 * @param a first word state
 * @param b second word state
 * @return true if the word states are directly adjacent
 */

private def adjacentHorizontal(a: WordState, b: WordState): Boolean =
	if a.startRow != b.startRow then
		return false

	val aStart = a.startColumn
	val aEnd = a.startColumn + a.word.length

	val bStart = b.startColumn
	val bEnd = b.startColumn + b.word.length

	math.abs(aEnd - bStart) == 1 || math.abs(bEnd - aStart) == 1

private def adjacentVertical(a: WordState, b: WordState): Boolean = {
	if a.startColumn != b.startColumn then
		return false

	val aStart = a.startRow
	val aEnd = a.startRow + a.word.length

	val bStart = b.startRow
	val bEnd = b.startRow + b.word.length

	math.abs(aEnd - bStart) == 1 || math.abs(bEnd - aStart) == 1
}
