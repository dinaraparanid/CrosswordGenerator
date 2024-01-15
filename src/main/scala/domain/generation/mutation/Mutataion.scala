package domain.generation.mutation

import data.generation.mutation.MutationSelection
import data.generation.population.{Table, TableState, WordState}
import domain.generation.crossover.tryPutCrossing
import domain.generation.generator
import domain.generation.population.{emptyTable, putWord, wordState}
import domain.generation.selection.{connectivityComponents, wordsCrosses}

import scala.annotation.tailrec
import scala.util.Random

private val MutationRate = 0.33F

/**
 * Performs mutation on a table state, introducing changes to its word placements.
 * Algorithm searches all connectivity components, using [[connectivityComponents]],
 * picks the last one as the component, from which first ceil(size * MUTATION_RATE)
 * words have to be intersected with other connectivity components.
 * For such mutated words, it tries to cross them with others, using [[mergedOrNew()]].
 * In case of failure, position remains the same.
 *
 * @param tableState the table state to mutate
 * @return the mutated table state with improved fitness value
 */

def mutation(tableState: TableState) =
  val TableState(table, words) = tableState
  val newTable = emptyTable(table.length)

  val components = connectivityComponents(words)
  val MutationSelection(mutated, notMutated) = mutationSelection(components)

  val (hw, vw) = putWords(notMutated, newTable)
  var wordMap = wordStatesMap(notMutated)

  val (mergedNew, _, _) = mergedOrNew(mutated, newTable, hw, vw)
  wordMap ++= wordStatesMap(mergedNew)

  TableState(newTable, reorderedWordStates(words, wordMap))

/**
 * Choose connectivity components whose words
 * will be mutated and those that will stay the same,
 * then combines their words into two lists (mutated and not)
 *
 * @param components list of sets of words that are connected
 * @return lists of both mutated words and not mutated words
 * @see [[mutation]]
 */

private def mutationSelection(
  components: List[Set[WordState]]
)(using random: Random): MutationSelection =
  @tailrec
  def impl(
    comps:      List[Set[WordState]] = components,
    mutated:    List[WordState] = Nil,
    notMutated: List[WordState] = Nil
  ): (List[WordState], List[WordState]) =
    comps match
      case Nil ⇒ (mutated, notMutated)

      case head :: next
        if random.nextMutationRate <= MutationRate ⇒
        impl(next, mutated ++ head, notMutated)

      case head :: next ⇒
        impl(next, mutated, notMutated ++ head)

  val (mutatedWords, notMutatedWords) = impl()
  MutationSelection(mutatedWords, notMutatedWords)

/**
 * Attempts to intersect the given words
 * with placed ones, or leaves it as it is.
 *
 * @param wordsStates     mutated words, for which algorithm is applied
 * @param table           the table to place the word
 * @param horizontalWords list of horizontal word states
 * @param verticalWords   list of vertical word states
 * @return list of word states, either merged or kept the same,
 *         updated list of horizontal words,
 *         updated list of vertical words
 * @see [[mutation]]
 */

private def mergedOrNew(
  wordsStates:     List[WordState],
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): (List[WordState], List[WordState], List[WordState]) =
  @tailrec
  def impl(
    words: List[WordState] = wordsStates,
    ws:    List[WordState] = Nil,
    hws:   List[WordState] = horizontalWords,
    vws:   List[WordState] = verticalWords
  ): (List[WordState], List[WordState], List[WordState]) =
    words match
      case Nil ⇒ (ws, hws, vws)
      case head :: next ⇒
        val w = tryPutCrossing(head, table, hws, vws) getOrElse
          wordState(head.word, table, hws, vws)

        val (hw, vw) = putWord(w, table, hws, vws)
        impl(next, w :: ws, hw, vw)

  impl()

/**
 * Reorders the word states based on their
 * original order in the canonical order list
 *
 * @param canonicalOrder list of word states in their original order
 * @param wordStatesMap  map associating words to corresponding word states
 * @return list of word states in the original order
 */

private def reorderedWordStates(
  canonicalOrder: List[WordState],
  wordStatesMap:  Map[String, WordState]
): List[WordState] =
  canonicalOrder map (_.word) map wordStatesMap

extension (random: Random)
  /**
   * Generates mutation rate value
   * @return value in range [0 until 1]
   */

  def nextMutationRate: Float =
    random.between(0F, 1F)