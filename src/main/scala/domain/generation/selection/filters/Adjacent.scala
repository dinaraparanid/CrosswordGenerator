package domain.generation.selection.filters

import data.generation.population.WordState

/**
 * Checks whether two horizontal words are directly adjacent to each other
 *
 * @param a first word state
 * @param b second word state
 * @return true if the word states are directly adjacent
 */

def adjacentHorizontal(a: WordState, b: WordState): Boolean =
	if a.startRow != b.startRow then
		return false

	val aStart = a.startColumn
	val aEnd = a.startColumn + a.word.length

	val bStart = b.startColumn
	val bEnd = b.startColumn + b.word.length

	math.abs(aEnd - bStart) == 1 || math.abs(bEnd - aStart) == 1

/**
 * Checks whether two vertical words are directly adjacent to each other
 *
 * @param a first word state
 * @param b second word state
 * @return true if the word states are directly adjacent
 */

def adjacentVertical(a: WordState, b: WordState): Boolean =
	if a.startColumn != b.startColumn then
		return false

	val aStart = a.startRow
	val aEnd = a.startRow + a.word.length

	val bStart = b.startRow
	val bEnd = b.startRow + b.word.length

	math.abs(aEnd - bStart) == 1 || math.abs(bEnd - aStart) == 1
