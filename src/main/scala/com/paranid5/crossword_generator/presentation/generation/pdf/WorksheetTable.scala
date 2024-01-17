package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.paranid5.crossword_generator.data.generation.population.TableState

/**
 * Empty crossword worksheet that is
 * designed for words placement by user
 *
 * @param tableState from which pdf table is composed
 * @return generated pdf [[Table]]
 * @see [[CrosswordTable]]
 */

def WorksheetTable(tableState: TableState): Table =
  CrosswordTable(tableState, TextAlignment.LEFT)(_ ⇒ "")
