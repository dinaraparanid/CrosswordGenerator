package domain.generation.selection.filters

import data.generation.population.WordState
import domain.generation.selection.{columnSet, rowSet}

/**
 * Checks whether two horizontal word states
 * have a neighboring border greater than 1,
 * indicating an overlap
 *
 * @param a first word state
 * @param b second word state
 * @return true if the neighboring border is too large.
 */

def tooBigNeighbouringBorderHorizontal(a: WordState, b: WordState): Boolean =
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

def tooBigNeighbouringBorderVertical(a: WordState, b: WordState): Boolean =
	if math.abs(a.startColumn - b.startColumn) != 1 then
		return false

	val aCoords = rowSet(a)
	val bCoords = rowSet(b)

	(aCoords & bCoords).sizeIs > 1