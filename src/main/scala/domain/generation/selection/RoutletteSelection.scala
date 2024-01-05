package domain.generation.selection

import data.generation.population.TableState
import domain.generation.selection.fitness.{fitnessStates, fitnessSumsFromStates}

import scala.util.Random

/**
 * Selects a subset of table states from a given population
 * using the roulette wheel selection algorithm.
 * Algorithm computes fitness values for each table,
 * then finds prefix sums for all fitness values,
 * then applies roulette algorithm for fitness prefix sums
 *
 * @param population list of table states to select from
 * @return A list of selected table states
 */

def selectWithRoulette(population: Seq[TableState])(using random: Random): Seq[TableState] =
	val states = fitnessStates(population)
	val sums = fitnessSumsFromStates(states)
	val totalFitness = sums.last
	val rouletteValue = random nextRouletteValue totalFitness

	sums
		.zip(states)
		.filter { case (fitness, _) ⇒ fitness > rouletteValue }
		.map { case (_, tableSt) ⇒ tableSt.table }

extension (random: Random)
	/**
	 * Generates roulette value for the selection process
	 * @return value in range [0 until [[totalFitness]]]
	 */

	def nextRouletteValue(totalFitness: Float): Float =
		random.between(0, totalFitness)