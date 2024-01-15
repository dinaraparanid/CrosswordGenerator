package domain.generation.population

import data.generation.population.{Layout, Table, WordState}

/**
 * Places a word with the given positions and the layout into the table
 * and updates word lists to reflect the placement of a given word state
 *
 * @param wordState       word with its positions and the layout to place
 * @param table           table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @param verticalWords   vertical words placed in the table
 * @return updated paired horizontal and vertical word lists
 */

def putWord(
  wordState:       WordState,
  table:           Table,
  horizontalWords: List[WordState],
  verticalWords:   List[WordState]
): (List[WordState], List[WordState]) =
  wordState.layout match
    case Layout.HORIZONTAL ⇒
      (putWordHorizontal(wordState, table, horizontalWords), verticalWords)

    case Layout.VERTICAL ⇒
      (horizontalWords, putWordVertical(wordState, table, verticalWords))

/**
 * Places a word with the given positions horizontally into the table
 * and updates horizontal words to reflect the placement of a given word state
 *
 * @param wordState       word with its positions and the layout to place
 * @param table           table to place the word
 * @param horizontalWords horizontal words placed in the table
 * @return updated horizontal word list
 */

private def putWordHorizontal(
  wordState:       WordState,
  table:           Table,
  horizontalWords: List[WordState],
): List[WordState] =
  val WordState(word, row, column, _) = wordState

  (column until column + word.length) foreach: c ⇒
    table(row)(c) = word(c - column)

  wordState :: horizontalWords

/**
 * Places a word with the given positions vertically into the table
 * and updates vertical words to reflect the placement of a given word state
 *
 * @param wordState     word with its positions and the layout to place
 * @param table         table to place the word
 * @param verticalWords vertical words placed in the table
 * @return updated vertical word list
 */

private def putWordVertical(
  wordState:     WordState,
  table:         Table,
  verticalWords: List[WordState],
): List[WordState] =
  val WordState(word, row, column, _) = wordState

  (row until row + word.length) foreach: r ⇒
    table(r)(column) = word(r - row)

  wordState :: verticalWords