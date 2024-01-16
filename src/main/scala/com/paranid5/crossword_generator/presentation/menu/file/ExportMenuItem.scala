package com.paranid5.crossword_generator.presentation.menu.file

import com.paranid5.crossword_generator.data.app.SessionBroadcast
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, sessionDocPathStream}
import com.paranid5.crossword_generator.presentation.sessionBroadcast
import com.paranid5.crossword_generator.presentation.ui.utils.ctrlShiftKey

import zio.{RIO, Runtime, Scope, URIO, Unsafe, ZIO}

import java.awt.event.KeyEvent
import java.io.File
import java.nio.file.Files
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.{JFileChooser, JMenuItem}

private def ExportMenuItem(): URIO[StoragePreferences & SessionBroadcast & Scope, JMenuItem] =
  val menuItem = new JMenuItem("Export"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_S))

  def recompose(
    runtime: Runtime[StoragePreferences & SessionBroadcast],
    docPath: String
  ): Unit =
    menuItem addActionListener: _ ⇒
      Unsafe.unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            FileSaveDialog(docPath)

  for
    runtime ← (StoragePreferences.layer ++ SessionBroadcast.layer).toRuntime
    doc     ← sessionDocPathStream
    _       ← doc
      .foreach:
        ZIO attempt recompose(runtime, _)
      .fork
  yield menuItem

private def FileSaveDialog(sessionDocPath: String): RIO[StoragePreferences & SessionBroadcast, Option[Unit]] =
  val fileChooser = new JFileChooser:
    setFileFilter(FileNameExtensionFilter(null, "pdf"))

  val response = fileChooser showSaveDialog null

  ZIO.when(response == JFileChooser.APPROVE_OPTION):
    val storeFile = fileChooser.getSelectedFile.toPdf
    Files.copy(File(sessionDocPath).toPath, storeFile.toPath)

    for
      session ← sessionBroadcast()
      _       ← session.updatePage()
    yield ()

extension (file: File)
  private def toPdf: File =
    val path = file.getAbsolutePath
    if path endsWith ".pdf" then file
    else File(s"$path.pdf")