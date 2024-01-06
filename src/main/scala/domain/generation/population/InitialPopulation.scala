package domain.generation.population

import data.generation.population.*
import domain.generation.generator
import domain.generation.population.startCoords
import domain.generation.selection.canPutWord

import scala.annotation.tailrec
import scala.util.Random

private val PopulationSize = 100
val EmptyLetter = '_'

/**
 * Creates an initial population of table states
 * by randomly generating a specified number of tables using the provided words list.
 * All generated tables consist of correctly inserted words:
 * no overlapping with neighbours and no overlapping with borders.
 * Some words may cross each other, graph connectivity is not guaranteed
 *
 * @param words  list of words to use for table generation
 * @return A list of randomly generated table states
 */

def initialPopulation(words: List[String], tableSize: Int): Seq[TableState] =
  LazyList continually { wordTable(words, tableSize) } take PopulationSize

/**
 * Generates a table state with randomly placed words from a given list of words.
 * Generated table consists of correctly inserted words:
 * no overlapping with neighbours and no overlapping with borders.
 * Some words may cross each other, graph connectivity is not guaranteed
 *
 * @param words  list of words to place in the table
 * @return A table state representing the generated table
 */

private def wordTable(words: List[String], tableSize: Int): TableState =
  val table = emptyTable(tableSize)
  TableState(table, wordStates(words, table))

/**
 * Constructs [[EmptyLetter]] squared table with given size
 * @return [[Table]] of size [[tableSize]] filled with [[EmptyLetter]]
 */

def emptyTable(tableSize: Int): Table =
  Array.fill(tableSize) { Array.fill(tableSize)(EmptyLetter) }

/**
 * Generates random positions for words,
 * randomly places them into the provided table
 * and updates the corresponding word lists.
 * For each word there is no overlapping with neighbours and no overlapping with borders.
 * Some words may cross each other, graph connectivity is not guaranteed
 *
 * @param words           list of words to place in the table
 * @param table           table in which to place the words
 * @return list of word states representing the placed words
 */

private def wordStates(words: List[String], table: Table): List[WordState] =
  @tailrec
  def impl(
    words: List[String] = words,
    ws:    List[WordState] = Nil,
    hws:   List[WordState] = Nil,
    vws:   List[WordState] = Nil
  ): (List[WordState], List[WordState], List[WordState]) =
    words match
      case Nil ⇒ (ws, hws, vws)
      case head :: next ⇒
        val w = wordState(head, table, hws, vws)
        val (hw, vw) = putWord(w, table, hws, vws)
        impl(next, w :: ws, hw, vw)

  impl()._1

/**
 * Generates random position for the given word,
 * randomly places it into the provided table
 * and updates the corresponding word lists.
 * For each word there is no overlapping with neighbours and no overlapping with borders.
 * Some words may cross each other, graph connectivity is not guaranteed
 *
 * @param word            word to be placed in the table
 * @param table           table in which to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @param random          random generator
 * @return The word state representing the placed word,
 *         updated list of horizontally placed words,
 *         updated list of vertically placed words
 */

def wordState(
  word:            String,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState],
)(using random: Random): WordState =
  @tailrec
  def impl(): WordState =
    val layout = random.nextLayout
    val Coords(row, column, _) = startCoords(word, table.length, layout)

    if tryPutWord(row, column, layout) then
      return WordState(word, row, column, layout)

    impl()

  @inline
  def tryPutWord(startRow: Int, startColumn: Int, layout: Layout): Boolean =
    canPutWord(
      word = word,
      startRow = startRow,
      startColumn = startColumn,
      layout = layout,
      table = table,
      horizontalWords = horizontalWords,
      verticalWords = verticalWords
    )

  impl()
