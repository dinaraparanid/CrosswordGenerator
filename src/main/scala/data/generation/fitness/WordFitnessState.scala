package data.generation.fitness

import data.generation.population.WordState

/**
 * Word state with its fitness
 *
 * @param word    the word itself
 * @param fitness word's fitness
 */

case class WordFitnessState(word: WordState, fitness: Float)
