package presentation.generation

import data.app.SessionStates
import domain.session.SessionDocumentWriter
import presentation.{pageChannel, sessionDoc}

import org.icepdf.ri.common.{SwingController, SwingViewBuilder}

import zio.channel.foreverWhile
import zio.{RIO, ZIO}

import javax.swing.JSplitPane

def CrosswordSheetView(): RIO[SessionStates, JSplitPane] =
  def documentController(doc: String): SwingController =
    new SwingController:
      openDocument(doc)

  def documentPane(controller: SwingController): JSplitPane =
    SwingViewBuilder(controller)
      .buildUtilityAndDocumentSplitPane(true)

  for
    doc        ← initialDoc()
    controller ← ZIO attempt documentController(doc)
    view       ← ZIO attempt documentPane(controller)
    _          ← monitorPageChanges(controller).fork
  yield view

private def initialDoc(): RIO[SessionStates, String] =
  for
    doc ← sessionDoc()
    _   ← ZIO attempt SessionDocumentWriter(doc).close()
  yield doc

private def monitorPageChanges(controller: SwingController): RIO[SessionStates, Unit] =
  foreverWhile:
    for
      pageChan ← pageChannel()
      data     ← pageChan.receive mapError (err ⇒ Exception(err.toString))
      doc      ← sessionDoc()
      _        ← ZIO attempt revalidateCrosswordPage(controller, doc, data)
    yield data

private def revalidateCrosswordPage(
  controller: SwingController,
  doc:        String,
  makeUpd:    Boolean
): Unit =
  if makeUpd then
    controller openDocument doc
