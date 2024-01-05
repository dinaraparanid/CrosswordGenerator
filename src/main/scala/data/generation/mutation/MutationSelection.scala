package data.generation.mutation

import data.generation.population.WordState

/**
 * Container for those words that will be mutated
 * and those that will stay the same
 *
 * @param mutatedWords    words that will be mutated
 * @param notMutatedWords words that will stay the same
 */

case class MutationSelection(
	mutatedWords: List[WordState],
	notMutatedWords: List[WordState]
)
