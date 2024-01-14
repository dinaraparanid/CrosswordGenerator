package presentation.generation.pdf

import com.itextpdf.layout.element.Table
import data.generation.population.TableState

def AnswerTable(tableState: TableState): Table =
  CrosswordTable(tableState)(_.toString)
