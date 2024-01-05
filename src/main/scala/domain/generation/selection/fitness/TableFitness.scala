package domain.generation.selection.fitness

import data.generation.fitness.TableFitnessState
import data.generation.population.TableState
import domain.generation.selection.{longestConnectivity, wordsCoords, wordsCrosses}

private val FitnessWordCriteriaAmount = 2
private val MaxFitnessCriteriaWeight = 1F

/**
 * Calculates maximum fitness value with corresponding index
 *
 * @param population The list of table states to evaluate
 * @return the maximum fitness value and its corresponding index
 */

def maxFitness(population: Seq[TableState]): (Float, Int) =
	maxFitnessFromValues(fitnessValues(population))

/**
 * Calculates maximum fitness value with corresponding index
 *
 * @param fitnessValues the list of fitness values
 * @return the maximum fitness value and its corresponding index
 */

private def maxFitnessFromValues(fitnessValues: Seq[Float]): (Float, Int) =
	fitnessValues.zipWithIndex.maxOption.getOrElse((0F, 0))

/**
 * Calculates the fitness prefix sums for a given population
 *
 * @param population The list of table states to evaluate
 * @return A list of the cumulative fitness sums (prefix sums)
 */

def fitnessSums(population: Seq[TableState]): Seq[Float] =
	fitnessSumsFromValues(fitnessValues(population))

/**
 * Calculates the fitness prefix sums for a given list of fitness states
 *
 * @param states The list of population's fitness values
 * @return A list of the cumulative fitness sums (prefix sums)
 */

def fitnessSumsFromStates(states: Seq[TableFitnessState]) =
	fitnessSumsFromValues(fitnessValuesFromStates(states))

/**
 * Calculates the fitness state of each table state in a given population
 *
 * @param population The list of table states to evaluate
 * @return A list of fitness states representing the fitness of each table
 */

def fitnessStates(population: Seq[TableState]): Seq[TableFitnessState] =
	population map fitnessState

/**
 * Extracts the fitness values from the population
 *
 * @param population The list of table states to evaluate
 * @return A list of fitness values
 */

private def fitnessValues(population: Seq[TableState]): Seq[Float] =
	fitnessValuesFromStates(fitnessStates(population))

/**
 * Extracts the fitness values from a list of table fitness states
 *
 * @param fitnessStates The list of table fitness states
 * @return A list of fitness values
 */

private def fitnessValuesFromStates(fitnessStates: Seq[TableFitnessState]): Seq[Float] =
	fitnessStates map (_.totalFitness)

/**
 * Calculates the fitness prefix sums for a given list of fitness values
 *
 * @param fitnessVals The list of fitness values
 * @return A list of the cumulative fitness sums (prefix sums)
 */

private def fitnessSumsFromValues(fitnessVals: Seq[Float]): Seq[Float] =
	fitnessVals.scan(0F) { _ + _ }.tail

/**
 * Calculates the fitness of a given table
 * based on word placement and connectivity
 *
 * @param tableState The table state to evaluate
 * @return table with its fitness results (each words' fitness + graph connectivity)
 */

private def fitnessState(tableState: TableState): TableFitnessState =
	val TableState(table, wordStates) = tableState
	val coords = wordsCoords(wordStates)
	val crosses = wordsCrosses(coords)
	val fitness = wordsFitnessStates(wordStates, table, crosses)
	val connectivity = longestConnectivity(crosses).toFloat
	val graphConnectivity = connectivity / wordStates.length
	TableFitnessState(tableState, fitness, graphConnectivity)
