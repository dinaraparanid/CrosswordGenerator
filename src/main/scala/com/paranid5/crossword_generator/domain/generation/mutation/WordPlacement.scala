package com.paranid5.crossword_generator.domain.generation.mutation

import com.paranid5.crossword_generator.data.generation.population.{Table, WordState}
import com.paranid5.crossword_generator.domain.generation.population.putWord

import scala.annotation.tailrec

/**
 * Places words with the given positions and the layouts into the table
 * and updates word lists to reflect the placement of a given word state
 *
 * @param wordStates      words with their positions and the layouts to place
 * @param table           table to place the word
 * @return horizontal and vertical word lists
 */

private def putWords(
  wordStates: List[WordState],
  table:      Table
): (List[WordState], List[WordState]) =
  @tailrec
  def impl(
    ws:  List[WordState] = wordStates,
    hws: List[WordState] = Nil,
    vws: List[WordState] = Nil
  ): (List[WordState], List[WordState]) =
    ws match
      case Nil ⇒ (hws, vws)
      case head :: next ⇒
        val (hw, vw) = putWord(head, table, hws, vws)
        impl(next, hw, vw)

  impl()

/**
 * Associates words with corresponding word states
 * @return Map of words as keys and  words states as values
 */

def wordStatesMap(wordStates: List[WordState]): Map[String, WordState] =
  wordStates.map { ws ⇒ (ws.word, ws) }.toMap
