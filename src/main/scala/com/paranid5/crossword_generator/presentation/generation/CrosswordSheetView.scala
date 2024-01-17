package com.paranid5.crossword_generator.presentation.generation

import com.paranid5.crossword_generator.data.app.SessionChannel
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, sessionDoc}
import com.paranid5.crossword_generator.domain.session.SessionDocumentWriter
import com.paranid5.crossword_generator.presentation.updatePageChannel

import org.icepdf.ri.common.{SwingController, SwingViewBuilder}

import zio.channel.foreverWhile
import zio.{RIO, ZIO}

import java.io.File
import javax.swing.JSplitPane

/**
 * Composes UI for the crossword pdf document
 * from pdf page itself and the utility panel
 *
 * @return [[RIO]] with [[JSplitPane]] of the whole widget
 *         that completes when the whole content is set
 */

def CrosswordSheetView(): RIO[StoragePreferences & SessionChannel, JSplitPane] =
  for
    doc        ← initialDoc()
    controller ← ZIO attempt DocumentController(doc)
    view       ← ZIO attempt DocumentPane(controller)
    _          ← monitorPageChanges(controller).fork
  yield view

/**
 * Creates a new [[SwingController]] for the crossword pdf document
 *
 * @param doc The path to the pdf document
 * @return [[SwingController]] with opened document
 */

@inline
private def DocumentController(doc: String): SwingController =
  new SwingController:
    openDocument(doc)

/**
 * Composes split pane with utility panel and document itself
 *
 * @param controller [[SwingController]] of the pdf document
 * @return [[JSplitPane]] with utility and document
 */

@inline
private def DocumentPane(controller: SwingController): JSplitPane =
  SwingViewBuilder(controller)
    .buildUtilityAndDocumentSplitPane(true)

/**
 * Retrieves the crossword pdf document path from the storage
 * @return session pdf document path
 */

@inline
private def initialDoc(): RIO[StoragePreferences, String] =
  for
    doc ← sessionDoc
    _   ← ZIO.when(!File(doc).exists()):
      ZIO attempt SessionDocumentWriter(doc).close()
  yield doc

/**
 * Continuously monitors the [[updatePageChannel]]
 * for changes and revalidates crossword UI table accordingly
 *
 * @param controller [[SwingController]] of the pdf document
 * @return [[RIO]] that never completes,
 *         it must be executed in the separate fiber
 * @see [[ZIO.fork]]
 */

@inline
private def monitorPageChanges(controller: SwingController): RIO[StoragePreferences & SessionChannel, Unit] =
  foreverWhile:
    for
      pageChan ← updatePageChannel()
      data     ← pageChan.receive mapError (err ⇒ Exception(err.toString))
      doc      ← sessionDoc
      _        ← ZIO attempt revalidateCrosswordPage(controller, doc, data)
    yield data

/**
 * Reopens updated document
 *
 * @param controller [[SwingController]] of the pdf document
 * @param doc        path to the document
 * @param makeUpd    is update applied
 */

@inline
private def revalidateCrosswordPage(
  controller: SwingController,
  doc:        String,
  makeUpd:    Boolean
): Unit =
  if makeUpd then
    controller openDocument doc
