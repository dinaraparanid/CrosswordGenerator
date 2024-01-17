package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.Table
import com.paranid5.crossword_generator.data.generation.population.TableState

/**
 * Crossword table with all words inserted
 *
 * @param tableState from which pdf table is composed
 * @return generated pdf [[Table]]
 * @see [[CrosswordTable]]
 */

def AnswerTable(tableState: TableState): Table =
  CrosswordTable(tableState)(_.toString)
