package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.Table
import com.paranid5.crossword_generator.data.generation.population.TableState

def AnswerTable(tableState: TableState): Table =
  CrosswordTable(tableState)(_.toString)
