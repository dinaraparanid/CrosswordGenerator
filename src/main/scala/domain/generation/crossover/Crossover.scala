package domain.generation.crossover

import data.generation.population.{Table, TableState, WordState}
import domain.generation.generator
import domain.generation.population.{emptyTable, putWord, wordState}

import scala.annotation.tailrec

/**
 * Performs crossover between two parent tables to generate an offspring table state.
 * Algorithm alternates words between two tables, attempting to place them in the same position
 * as it was in the parent. In case of failure, it tries to cross any word that is already in the table.
 * If fails again, randomly picks any position using [[wordState]]
 *
 * @param parent1 the first parent table state
 * @param parent2 the second parent table state
 * @return The offspring table state
 * @see [[crossoverWordStates]], [[crossoverWordState]]
 */

def crossover(parent1: TableState, parent2: TableState): TableState =
  val table = emptyTable(parent1.table.length)
  val states = crossoverWordStates(parent1, parent2, table)
  TableState(table, states)

/**
 * Generates the offspring list of words, produced from the given parent tables.
 * Algorithm alternates words between two tables, attempting to place them in the same position
 * as it was in the parent. In case of failure, it tries to cross any word that is already in the table.
 * If fails again, randomly picks any position using [[wordState]]
 *
 * @param parent1 the first parent table state
 * @param parent2 the second parent table state
 * @param table   table to place the word
 * @return The offspring table state
 * @see [[crossover]], [[crossoverWordState]]
 */

private def crossoverWordStates(
  parent1: TableState,
  parent2: TableState,
  table:   Table,
): List[WordState] =
  @tailrec
  def impl(
    p1:   List[WordState] = parent1.words,
    p2:   List[WordState] = parent2.words,
    turn: Int = 0,
    ws:   List[WordState] = Nil,
    hws:  List[WordState] = Nil,
    vws:  List[WordState] = Nil
  ): (List[WordState], List[WordState], List[WordState]) =
    turn match
      case i if i % 2 == 0 ⇒
        p1 match
          case Nil ⇒ (ws, hws, vws)
          case head :: next ⇒
            val w = crossoverWordState(head, table, hws, vws)
            val (hw, vw) = putWord(w, table, hws, vws)
            impl(next, p2.tail, turn + 1, w :: ws, hw, vw)

      case _ ⇒
        p2 match
          case Nil ⇒ (ws, hws, vws)
          case head :: next ⇒
            val w = crossoverWordState(head, table, hws, vws)
            val (hw, vw) = putWord(w, table, hws, vws)
            impl(p1.tail, next, turn + 1, w :: ws, hw, vw)

  impl()._1

/**
 * Generates the offspring word based on already placed words in the table.
 * Algorithm alternates words between two tables, attempting to place them in the same position
 * as it was in the parent. In case of failure, it tries to cross any word that is already in the table.
 * If fails again, randomly picks any position using [[wordState]]
 *
 * @param table           table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return The offspring table state
 * @see [[crossoverWordStates]]
 */

private def crossoverWordState(
  wordSt:          WordState,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): WordState =
  List(
    tryPutWithSamePosition(wordSt, table, horizontalWords, verticalWords),
    tryPutCrossing(wordSt, table, horizontalWords, verticalWords)
  )
    .flatten
    .headOption
    .getOrElse(wordState(wordSt.word, table, horizontalWords, verticalWords))
