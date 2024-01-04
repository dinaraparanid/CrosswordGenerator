package data.generation.population

/**
 * The state of a crossword's table
 *
 * @param table table with placed words
 * @param words words' states, placed in the table
 */

case class TableState(table: Table, words: List[WordState])

type Table = Array[Array[Char]]

enum Layout:
	case HORIZONTAL, VERTICAL
