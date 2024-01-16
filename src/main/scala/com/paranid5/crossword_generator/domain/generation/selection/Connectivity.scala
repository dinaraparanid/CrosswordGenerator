package com.paranid5.crossword_generator.domain.generation.selection

import com.paranid5.crossword_generator.data.generation.population.WordState

import scala.annotation.tailrec

/**
 * Determines the length of the longest connectivity component
 * in the word placement graph
 *
 * @param wordsCrosses map representing the words' intersections
 * @return length of the longest connected component
 */

private def longestConnectivity(wordsCrosses: WordsCrosses): Int =
  connectivityComponents(wordsCrosses)
    .map(_.size)
    .maxOption
    .getOrElse(0)

def connectivityComponents(wordStates: List[WordState]): List[Set[WordState]] =
  connectivityComponents(wordsCrosses(wordStates))

/**
 * Generates a stream of connected graph components,
 * represented as sets of word states,
 * from a map of words' intersections
 *
 * @param wordsCrosses map representing the words' intersections
 * @return stream of sets of word states, each representing a connectivity component
 */

private def connectivityComponents(wordsCrosses: WordsCrosses): List[Set[WordState]] =
  @tailrec
  def impl(
    allWords:   Iterable[WordState] = wordsCrosses.keys,
    allVisited: Set[WordState] = Set.empty,
    components: List[Set[WordState]] = Nil
  ): List[Set[WordState]] =
    allWords.headOption match
      case None ⇒ components

      case Some(head)
        if allVisited contains head ⇒
        impl(allWords.tail, allVisited, components)

      case Some(head) ⇒
        val newVisited = dfs(head, wordsCrosses)

        impl(
          allWords = allWords.tail,
          allVisited = allVisited ++ newVisited,
          components = newVisited :: components
        )

  impl()

/**
 * Performs a depth-first search traversal
 * starting from the given word
 * and produces the set of connected word states
 *
 * @param start        the word state from which to start the DFS traversal
 * @param wordsCrosses map representing the words' intersections
 * @return set of word states connected to the starting word state
 */

private def dfs(
  start:        WordState,
  wordsCrosses: WordsCrosses
): Set[WordState] =
  @inline
  def impl(current: WordState = start, walked: Set[WordState] = Set.empty): Set[WordState] =
    if walked contains current then
      return walked

    val walkedUpd = walked + current
    val notWalked = wordsCrosses(current) &~ walkedUpd
    collect(notWalked, walkedUpd)

  @tailrec
  def collect(
    notTraversed: Set[WordState],
    traversed:    Set[WordState]
  ): Set[WordState] =
    notTraversed.headOption match
      case None ⇒ traversed
      case Some(head) ⇒
        collect(notTraversed.tail, impl(head, traversed))

  impl()
