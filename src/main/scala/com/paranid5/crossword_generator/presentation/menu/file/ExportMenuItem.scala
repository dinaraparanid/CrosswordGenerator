package com.paranid5.crossword_generator.presentation.menu.file

import com.paranid5.crossword_generator.data.app.SessionChannel
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, sessionDocPathStream}
import com.paranid5.crossword_generator.presentation.sessionChannel
import com.paranid5.crossword_generator.presentation.ui.utils.ctrlShiftKey

import zio.{RIO, Runtime, URIO, Unsafe, ZIO}

import java.awt.event.KeyEvent
import java.io.File
import java.nio.file.Files
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.{JFileChooser, JMenuItem}

/**
 * Composes export to PDF menu item
 * @return [[URIO]] with [[JMenuItem]]
 *         that completes when all content is applied
 */

def ExportMenuItem(): URIO[StoragePreferences & SessionChannel, JMenuItem] =
  val menuItem = new JMenuItem("Export"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_S))

  val runtime = Runtime.default

  @inline
  def recompose(
    sessionChannel: SessionChannel,
    docPath:        String
  ): Unit =
    menuItem addActionListener: _ ⇒
      Unsafe.unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            FileSaveDialog(sessionChannel, docPath)

  for
    chan ← sessionChannel()
    doc  ← sessionDocPathStream
    _    ← doc
      .foreach:
        ZIO attempt recompose(chan, _)
      .fork
  yield menuItem

/**
 * File chooser dialog that
 * will store session pdf document
 *
 * @param sessionChan    page updates broadcast
 * @param sessionDocPath path of the pdf to export
 * @return Unit if selection was approved
 */

private def FileSaveDialog(
  sessionChan:    SessionChannel,
  sessionDocPath: String
): RIO[Any, Option[Unit]] =
  val fileChooser = new JFileChooser:
    setFileFilter(FileNameExtensionFilter(null, "pdf"))

  val response = fileChooser showSaveDialog null

  ZIO.when(response == JFileChooser.APPROVE_OPTION):
    val storeFile = fileChooser.getSelectedFile.toPdf
    Files.copy(File(sessionDocPath).toPath, storeFile.toPath)
    sessionChan.updatePage()

extension (file: File)
  /**
   * Checks if file has ".pdf" extension,
   * adds it if does not
   */

  private def toPdf: File =
    val path = file.getAbsolutePath
    if path endsWith ".pdf" then file
    else File(s"$path.pdf")