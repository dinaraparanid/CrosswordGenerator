package presentation.generation.input

import data.storage.{StoragePreferences, storeTitleInput, titleInput}
import presentation.ui.utils.PlaceholderTextComponent

import zio.{RIO, Runtime, Scope, Unsafe, ZIO}

import javax.swing.JTextField

private val TitlePlaceholder = "Crossword title"

def TitleInput(): RIO[StoragePreferences & Scope, JTextField] =
  val input = initialInputField
  val rt = Runtime.default

  def impl(
    initialText: String,
    runtime:     Runtime[StoragePreferences]
  ): Unit =
    input setText initialText
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeTitleInput(input.getText)

  for
    title   ← titleInput
    runtime ← StoragePreferences.layer.toRuntime
    _       ← ZIO attempt impl(title, runtime)
  yield input

private def initialInputField: JTextField =
  new JTextField
    with PlaceholderTextComponent:
    setPlaceholder(TitlePlaceholder)