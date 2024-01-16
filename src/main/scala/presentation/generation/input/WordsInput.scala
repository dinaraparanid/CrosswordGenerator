package presentation.generation.input

import data.storage.{StoragePreferences, storeWordsInput, wordsInput}
import presentation.ui.utils.PlaceholderTextComponent

import zio.{RIO, Runtime, Scope, Unsafe, ZIO}

import java.awt.BorderLayout
import javax.swing.{JPanel, JScrollPane, JTextArea, ScrollPaneConstants}

private val WordsPlaceholder = "Type or paste your words here"

def WordsInput(): RIO[StoragePreferences & Scope, JPanel] =
  val input = initialInputArea

  val panel = new JPanel(BorderLayout()):
    add(inputScroll(input), BorderLayout.CENTER)

  def impl(
    initialWords: String,
    runtime:      Runtime[StoragePreferences]
  ): Unit =
    input setText initialWords
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeWordsInput(input.getText)

  for
    words   ← wordsInput
    runtime ← StoragePreferences.layer.toRuntime
    _       ← ZIO attempt impl(words, runtime)
  yield panel

private def initialInputArea: JTextArea =
  new JTextArea
    with PlaceholderTextComponent:
    setPlaceholder(WordsPlaceholder)
    setWrapStyleWord(true)
    setLineWrap(true)

private def inputScroll(input: JTextArea): JScrollPane =
  new JScrollPane(input):
    setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED)
    setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)