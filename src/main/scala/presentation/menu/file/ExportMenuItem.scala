package presentation.menu.file

import data.app.{SessionStates, resetDocPath}
import presentation.{pageChannel, sessionDocStream, sessionStates}
import presentation.ui.utils.ctrlShiftKey

import zio.channel.Channel
import zio.{RIO, Runtime, URIO, Unsafe, ZIO}

import java.io.File
import java.awt.event.KeyEvent
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.{JFileChooser, JMenuItem}

private def ExportMenuItem(): URIO[SessionStates, JMenuItem] =
  val menuItem = new JMenuItem("Export"):
    setAccelerator(ctrlShiftKey(KeyEvent.VK_S))

  val runtime = Runtime.default

  def recompose(
    sessionStates: SessionStates,
    pageChan:      Channel[Boolean],
    docPath:       String,
  ): Unit =
    menuItem addActionListener: _ ⇒
      Unsafe.unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            FileSaveDialog(sessionStates, pageChan, docPath)

  for
    session  ← sessionStates()
    pageChan ← pageChannel()
    doc      ← sessionDocStream()
    _        ← doc
      .foreach:
        ZIO attempt recompose(session, pageChan, _)
      .fork
  yield menuItem

private def FileSaveDialog(
  sessionStates:  SessionStates,
  pageChan:       Channel[Boolean],
  sessionDocPath: String,
): RIO[Any, Option[Unit]] =
  val fileChooser = new JFileChooser:
    setFileFilter(FileNameExtensionFilter(null, "pdf"))

  val response = fileChooser showSaveDialog null

  ZIO.when(response == JFileChooser.APPROVE_OPTION):
    val storeFile = fileChooser.getSelectedFile.toPdf
    val storePath = storeFile.getAbsolutePath
    File(sessionDocPath) renameTo storeFile

    for
      _ ← sessionStates resetDocPath storePath
      _ ← (pageChan send true) mapError:
        err ⇒ Exception(err.toString)
    yield ()

extension (file: File)
  private def toPdf: File =
    val path = file.getAbsolutePath
    if path endsWith ".pdf" then file
    else File(s"$path.pdf")