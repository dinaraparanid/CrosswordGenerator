package com.paranid5.crossword_generator.presentation.generation

import com.paranid5.crossword_generator.data.app.SessionBroadcast
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, sessionDoc}
import com.paranid5.crossword_generator.domain.session.SessionDocumentWriter
import com.paranid5.crossword_generator.presentation.updatePageChannel

import org.icepdf.ri.common.{SwingController, SwingViewBuilder}

import zio.channel.foreverWhile
import zio.{RIO, ZIO}

import java.io.File
import javax.swing.JSplitPane

def CrosswordSheetView(): RIO[StoragePreferences & SessionBroadcast, JSplitPane] =
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

private def initialDoc(): RIO[StoragePreferences, String] =
  for
    doc ← sessionDoc
    _   ← ZIO.when(!File(doc).exists()):
      ZIO attempt SessionDocumentWriter(doc).close()
  yield doc

private def monitorPageChanges(controller: SwingController): RIO[StoragePreferences & SessionBroadcast, Unit] =
  foreverWhile:
    for
      pageChan ← updatePageChannel()
      data     ← pageChan.receive mapError (err ⇒ Exception(err.toString))
      doc      ← sessionDoc
      _        ← ZIO attempt revalidateCrosswordPage(controller, doc, data)
    yield data

private def revalidateCrosswordPage(
  controller: SwingController,
  doc:        String,
  makeUpd:    Boolean
): Unit =
  if makeUpd then
    controller openDocument doc
