package com.paranid5.crossword_generator.data.generation.population

/**
 * A specific character's coordinate within a table
 *
 * @param row    zero-based row index of the character
 * @param column zero-based column index of the character
 * @param letter character itself
 */

case class Coords(row: Int, column: Int, letter: Char)

/**
 * Word's coordinates and the layout within a table
 *
 * @param startRow    zero-based row index of the coordinate
 * @param startColumn zero-based column index of the coordinate
 * @param layout      layout of the word: 0 - horizontal, 1 - vertical
 */

case class CoordsWithLayout(startRow: Int, startColumn: Int, layout: Layout)
