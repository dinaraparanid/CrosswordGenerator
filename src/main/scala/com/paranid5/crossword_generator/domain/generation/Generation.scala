package com.paranid5.crossword_generator.domain.generation

import com.paranid5.crossword_generator.data.generation.population.{TableState, WordState}
import com.paranid5.crossword_generator.domain.generation.mutation.wordStatesMap
import com.paranid5.crossword_generator.domain.generation.population.{initialPopulation, nextPopulation}
import com.paranid5.crossword_generator.domain.generation.selection.selectWithRoulette
import com.paranid5.crossword_generator.domain.generation.selection.fitness.maxFitness

import scala.annotation.tailrec
import scala.util.Random

/** graph connectivity + (words crossing + not following) */

private val MaxFitness = 2F

given generator: Random = Random

/**
 * Generates crossword table, applying genetic algorithm
 *
 * @param words list of words to place in the crossword table
 * @param tableSize required table size
 */

def generation(words: List[String], tableSize: Int): TableState =
  @tailrec
  def impl(): TableState =
    val population = initialPopulation(words, tableSize)
    val selected = selectWithRoulette(population)
    val next = nextPopulation(selected)
    val (max, index) = maxFitness(next)

    if max >= MaxFitness then
      val TableState(table, ws) = next(index)
      val ordWs = reorderWordStates(words, ws)
      return TableState(table, ordWs)

    impl()

  impl()

/**
 * Reorders the word states based on their
 * original order in the canonical order list
 *
 * @param canonicalOrder list of words from the input
 * @param wordStates     word states in the table
 * @return list of word states in the original order
 */

private def reorderWordStates(
  canonicalOrder: List[String],
  wordStates:     List[WordState]
): List[WordState] =
  val wordMap = wordStatesMap(wordStates)
  canonicalOrder map wordMap
