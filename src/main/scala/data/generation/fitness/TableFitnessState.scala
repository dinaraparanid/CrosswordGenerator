package data.generation.fitness

import data.generation.population.TableState

/**
 * Fitness values of a table
 *
 * @param table        the table itself
 * @param wordsFitness list of words' fitness scores (number in range [0;1])
 * @param graphFitness fitness score of the words' connection graph
 *                     (longest connectivity divided by all words, number in range [0;1])
 */

case class TableFitnessState(
	table:        TableState,
	wordsFitness: List[WordFitnessState],
	graphFitness: Float
):
	/**
	 * Calculates the total table's fitness,
	 * which is the sum of graph fitness
	 * and the average of the words' fitness
	 *
	 * @return the total fitness score of the table
	 */

	def totalFitness: Float =
		graphFitness + (totalWordsFitness / wordsFitness.length)

	/**
	 * Calculates the sum of the words' fitness
	 *
	 * @return The sum of the words' fitness
	 * @see [[totalFitness]]
	 */

	private def totalWordsFitness: Float =
		wordsFitness.map(_.fitness).sum
