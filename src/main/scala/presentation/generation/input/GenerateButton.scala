package presentation.generation.input

import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.{Cell, Paragraph, Table}
import com.itextpdf.layout.properties.{HorizontalAlignment, TextAlignment}

import data.app.{SessionStates, isInputCorrectStream}
import data.generation.population.{TableState, Table as CrosswordTable}

import domain.generation.generation
import domain.generation.population.EmptyLetter
import domain.session.packing.packed
import domain.session.{SessionDocumentWriter, parsedWordsWithMeanings}

import presentation.sessionStates
import presentation.ui.utils.{combine, removeActionListeners}

import zio.channel.Channel
import zio.{RIO, Runtime, UIO, URIO, Unsafe, ZIO}

import javax.swing.JButton

import scala.annotation.tailrec
import scala.util.Using

private val DrainedCell = '*'

def GenerateButton(): URIO[SessionStates, JButton] =
  val button = new JButton("Generate"):
    putClientProperty("JButton.buttonType", "roundRect")

  val runtime = Runtime.default

  def recompose(
    isCrosswordCorrect: Boolean,
    titleInput:         String,
    wordsInput:         String,
    sessionDoc:         String,
    pageChan:           Channel[Boolean]
  ): Unit =
    button setEnabled isCrosswordCorrect
    button.removeActionListeners()
    button addActionListener: _ ⇒
      Unsafe.unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          for {
            table ← generateCrossword(wordsInput)
            _     ← storeCrossword(
              docPath = sessionDoc,
              titleInput = titleInput,
              tableState = table,
              pageChan = pageChan
            )
          } yield ()
      }

  for
    session           ← sessionStates()
    isCorrectStream   = session.isInputCorrectStream
    titleInputStream  = session.titleInput.changes
    wordsInputsStream = session.wordsInput.changes
    sessionDocStream  = session.sessionDoc.changes
    pageChan          = session.pageChan

    _ ← combine(
      isCorrectStream,
      titleInputStream,
      wordsInputsStream,
      sessionDocStream
    )
      .foreach { case (correct, title, words, doc) ⇒
        ZIO attempt recompose(correct, title, words, doc, pageChan)
      }
      .fork
  yield button

private def generateCrossword(wordsInput: String): UIO[TableState] =
  for
    wordsWithMeanings ← ZIO succeedBlocking
      parsedWordsWithMeanings(wordsInput)

    words ← ZIO succeedBlocking
      wordsWithMeanings.keys.toList

    tabSize ← ZIO succeedBlocking
      requiredTableSize(words)

    table ← ZIO succeedBlocking
      generation(words, tabSize)

    packedTab ← packed(table)
  yield packedTab

private def requiredTableSize(words: Iterable[String]): Int =
  val maxLength = words.map(_.length).max
  val size = words.size
  math.max(maxLength, size) * 2

private def storeCrossword(
  docPath:    String,
  titleInput: String,
  tableState: TableState,
  pageChan:   Channel[Boolean]
): RIO[Any, Unit] =
  Using(SessionDocumentWriter(docPath)) { doc ⇒
    doc add headerParagraph(titleInput)
    doc add answerTable(tableState)
  }

  (pageChan send true) mapError (err ⇒ Exception(err.toString))

private def headerParagraph(text: String): Paragraph =
  Paragraph(text)
    .setTextAlignment(TextAlignment.CENTER)
    .setFontSize(24)
    .setBold()

private def answerTable(tableState: TableState): Table =
  val pdfTable = Table(tableState.table.length)
    .setBorder(Border.NO_BORDER)
    .setHorizontalAlignment(HorizontalAlignment.CENTER)
    .setTextAlignment(TextAlignment.CENTER)
    .setFontSize(16)
    .setBold()

  val TableState(tab, _) = tableState
  val table = tab map (_.clone)

  def tableCell(row: Int, column: Int): Option[Cell] =
    val char = table(row)(column)

    char match
      case DrainedCell ⇒ Option.empty
      case EmptyLetter ⇒ Option(fatCell(table, row, column))
      case _           ⇒ Option(Cell() add Paragraph(char.toString))

  val cellList = for
    row  ← table.indices
    col  ← table.indices
    cell ← tableCell(row, col)
  yield cell

  @tailrec
  def putCells(
    cells:  List[Cell] = cellList.toList,
    pdfTab: Table = pdfTable
  ): Table =
    cells match
      case Nil          ⇒ pdfTab
      case head :: next ⇒
        putCells(next, pdfTab addCell head)

  putCells()

def fatCell(
  table:       CrosswordTable,
  startRow:    Int,
  startColumn: Int
): Cell =
  @tailrec
  def freeCellsInRow(
    row:       Int,
    curColumn: Int = startColumn,
    count:     Int = 0
  ): Int =
    if curColumn >= table.length then
      return count

    table(row)(curColumn) match
      case EmptyLetter ⇒ freeCellsInRow(row, curColumn + 1, count + 1)
      case _           ⇒ count

  @tailrec
  def freeRowsWithSameWidth(
    width:  Int,
    curRow: Int = startRow,
    count:  Int = 0
  ): Int =
    if curRow >= table.length then
      return count

    if freeCellsInRow(row = curRow) >= width then
      freeRowsWithSameWidth(width, curRow + 1, count + 1)
    else count

  val columns = freeCellsInRow(row = startRow)
  val rows    = freeRowsWithSameWidth(width = columns)

  for
    r ← startRow until startRow + rows
    c ← startColumn until startColumn + columns
  do table(r)(c) = DrainedCell

  Cell(rows, columns) setBorder Border.NO_BORDER
