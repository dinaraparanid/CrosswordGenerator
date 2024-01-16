package com.paranid5.crossword_generator.domain.generation.selection

import com.paranid5.crossword_generator.data.generation.population.{Coords, WordState}

/**
 *  Wrapper for a map associating each word
 *  with the set of words it intersects with
 */

type WordsCrosses = Map[WordState, Set[WordState]]

/**
 * Creates a map associating each word with
 * the set of word it intersects with,
 * applying [[wordCoords]]
 *
 * @param wordStates list of word states
 * @return map where each key represents a word
 *         and the value represents the set of word states it intersects with
 */

def wordsCrosses(wordStates: List[WordState]): WordsCrosses =
  wordsCrosses(wordsCoords(wordStates))

/**
 * Creates a map associating each word with the set of word it intersects with
 *
 * @param wordsCoords map where each key represents a word
 *                    and the value as every symbol's coordinates
 * @return map where each key represents a word
 *         and the value represents the set of word states it intersects with
 */

private def wordsCrosses(wordsCoords: Map[WordState, Set[Coords]]): WordsCrosses =
  wordsCoords map:
    case (word, coords) ⇒
      (word, crosses(word, coords, wordsCoords))

/**
 * Determines the word states that intersect
 * with a given word based on their coordinates
 *
 * @param wordState   the word state itself
 * @param coords      coordinates of every symbol of the word
 * @param wordsCoords A map associating each word with its coordinates
 * @return A list of words that intersect with the given word
 */

private def crosses(
  wordState:   WordState,
  coords:      Set[Coords],
  wordsCoords: Map[WordState, Set[Coords]]
): Set[WordState] =
  wordsCoords
    .filter { case (word, _) ⇒ word != wordState }
    .filter { case (_, crds) ⇒ crds exists coords.contains }
    .keySet
