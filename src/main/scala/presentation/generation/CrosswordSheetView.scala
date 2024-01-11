package presentation.generation

import data.app.SessionStates
import domain.session.SessionDocument
import presentation.sessionDoc

import org.icepdf.ri.common.{SwingController, SwingViewBuilder}

import zio.{RIO, ZIO}

import javax.swing.JSplitPane

def CrosswordSheetView(): RIO[SessionStates, JSplitPane] =
  def pdfDocumentPane(doc: String): JSplitPane =
    SwingViewBuilder(
      new SwingController:
        openDocument(doc)
    ).buildUtilityAndDocumentSplitPane(true)

  for {
    doc  ← initialDoc()
    view ← ZIO attempt pdfDocumentPane(doc)
  } yield view

private def initialDoc(): RIO[SessionStates, String] =
  for {
    doc ← sessionDoc()
    _   ← ZIO attempt SessionDocument(doc).close()
  } yield doc
