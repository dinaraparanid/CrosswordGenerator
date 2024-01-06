package domain.generation.selection.fitness

import data.generation.fitness.WordFitnessState
import data.generation.population.{Table, WordState}
import domain.generation.selection.WordsCrosses
import domain.generation.selection.filters.followed

/**
 * Constructs list of [[WordFitnessState]],
 * applying [[wordFitnessState]] on every word from [[wordStates]].
 * If the word is crossed at least once - result is 1, otherwise - 0.
 * If the word is followed with other words (e.g. "b_e_x_a_m" <- b not in "exam"),
 * then the result is 0, otherwise - 1
 *
 * @param wordStates   list of words to evaluate
 * @param wordsCrosses map representing the words' intersections
 * @return list of words' fitness states
 */

private def wordsFitnessStates(
	wordStates:   List[WordState],
	table:        Table,
	wordsCrosses: WordsCrosses
): List[WordFitnessState] =
	wordStates map (wordFitnessState(_, table, wordsCrosses))

/**
 * Calculates the fitness of each word,
 * considering words' crosses and borders with others.
 * If the word is crossed at least once - result is 1, otherwise - 0.
 * If the word is followed with other words (e.g. "b_e_x_a_m" <- b not in "exam"),
 * then the result is 0, otherwise - 1
 *
 * @param wordStates   list of words to evaluate
 * @param wordsCrosses map representing the words' intersections
 * @return list of fitness values for each word
 */

private def wordsFitness(
	wordStates:   List[WordState],
	table:        Table,
	wordsCrosses: WordsCrosses
): List[Float] =
	wordStates map (wordFitness(_, table, wordsCrosses))

/**
 * Constructs [[WordFitnessState]], applying [[wordFitness]]
 * If the word is crossed at least once - result is 1, otherwise - 0
 * If the word is followed with other words (e.g. "b_e_x_a_m" <- b not in "exam"),
 * then the result is 0, otherwise - 1
 *
 * @param wordState    the word state to evaluate
 * @param wordsCrosses map representing word intersections
 * @return word's fitness value
 */

private def wordFitnessState(
	wordState:    WordState,
	table:        Table,
	wordsCrosses: WordsCrosses
): WordFitnessState =
	WordFitnessState(
		word = wordState,
		fitness = wordFitness(wordState, table, wordsCrosses)
	)

/**
 * Calculates the word fitness based on the intersections.
 * If the word is crossed at least once - result is 1, otherwise - 0
 * If the word is followed with other words (e.g. "b_e_x_a_m" <- b not in "exam"),
 * then the result is 0, otherwise - 1
 *
 * @param wordState    the word state to evaluate
 * @param wordsCrosses map representing word intersections
 * @return word's fitness value
 */

private def wordFitness(
	wordState:    WordState,
	table:        Table,
	wordsCrosses: WordsCrosses
): Float =
	(crossed(wordState, wordsCrosses) + notFollowed(wordState, table))
		/ FitnessWordCriteriaAmount

/**
 * Determines whether a given word intersects with any other word.
 * If the word is crossed at least once - result is 1, otherwise - 0
 *
 * @param wordState    the word state to evaluate.
 * @param wordsCrosses map associating each word with the set of words it intersects with
 * @return fitness value indicating whether the word state is crossed by any other word
 */

private def crossed(
	wordState:    WordState,
	wordsCrosses: WordsCrosses
): Float =
	if !(wordsCrosses contains wordState) then 0F
	else if wordsCrosses(wordState).isEmpty then 0F
	else MaxFitnessCriteriaWeight

/**
 * Checks if the word is NOT adjacent with others.
 * Example: "b_e_x_a_m" - b does not belong to the word "exam"
 *
 * @param wordState word state to be placed onto the table
 * @param table     table in which to place the word
 * @return true if the word is NOT followed by other words
 */

private def notFollowed(wordState: WordState, table: Table) =
	if followed(wordState, table) then 0F else MaxFitnessCriteriaWeight