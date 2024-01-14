package presentation.generation.pdf

import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import data.generation.population.TableState

def WorksheetTable(tableState: TableState): Table =
  CrosswordTable(tableState, TextAlignment.LEFT)(_ ⇒ "")
