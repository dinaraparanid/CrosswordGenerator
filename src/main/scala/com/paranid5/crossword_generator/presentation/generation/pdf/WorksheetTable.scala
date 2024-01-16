package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.paranid5.crossword_generator.data.generation.population.TableState

def WorksheetTable(tableState: TableState): Table =
  CrosswordTable(tableState, TextAlignment.LEFT)(_ ⇒ "")
