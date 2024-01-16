package com.paranid5.crossword_generator.domain.generation.population

import com.paranid5.crossword_generator.data.generation.population.TableState
import com.paranid5.crossword_generator.domain.generation.crossover.crossover
import com.paranid5.crossword_generator.domain.generation.generator
import com.paranid5.crossword_generator.domain.generation.mutation.mutation

import scala.annotation.tailrec
import scala.util.Random

import java.util.NoSuchElementException

private def IncludeParentProbability = 0.25F

/**
 * Generates the next population of table states
 * based on the selected parents.
 * Applies both [[crossover]] and [[mutation]]
 *
 * @param selected selected table states to improve
 * @return table states representing the next generation
 */

def nextPopulation(selected: Seq[TableState]) =
  LazyList
    .continually(nextChildWithMbParent(selected))
    .flatten
    .take(PopulationSize)

/**
 * Generates a collection of child table states
 * by performing Applies both [[crossover]]
 * and [[mutation]] on selected parents.
 * With probability of [[IncludeParentProbability]],
 * may pick one random parent.
 *
 * @param selected selected table states
 * @return table states representing the next generation
 */

private def nextChildWithMbParent(
  selected: Seq[TableState]
)(using random: Random) =
  val parent1 = selected.getRandomly
  val parent2 = selected.getRandomly

  val child = crossover(parent1, parent2)
  val mutatedChild = mutation(child)

  var tables = List(mutatedChild)

  if random.nextParentInclude <= IncludeParentProbability then
    tables ::= List(parent1, parent2).getRandomly

  tables

extension[T] (seq: Seq[T])
  /**
   * Retrieves a random element from the sequence
   * @return randomly selected element from the list
   * @throws NoSuchElementException if the sequence is empty
   */

  def getRandomly(using random: Random): T =
    @tailrec
    def impl(s: Seq[T] = seq): T =
      s match
        case Seq() ⇒ throw NoSuchElementException("Sequence is empty")
        case Seq(elem) ⇒ elem
        case Seq(head, next*) ⇒
          if random.between(0F, 1F) < 0.5F then head else impl(next)

    impl()

extension (random: Random)
  /**
   * Generates value to peak parent into the new population
   * @return value in range [0 until 1]
   */

  def nextParentInclude: Float =
    random.between(0F, 1F)