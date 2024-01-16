package com.paranid5.crossword_generator.presentation.generation.input

import com.paranid5.crossword_generator.data.storage.{StoragePreferences, data, storeWordsInput, updateChannel, wordsInput}
import com.paranid5.crossword_generator.presentation.ui.utils.PlaceholderTextComponent

import zio.channel.Channel
import zio.{RIO, Runtime, Scope, Unsafe, ZIO}

import java.awt.BorderLayout
import javax.swing.{JPanel, JScrollPane, JTextArea, ScrollPaneConstants}
import scala.xml.Elem

private val WordsPlaceholder = "Type or paste your words here"

def WordsInput(): RIO[StoragePreferences & Scope, JPanel] =
  val input = initialInputArea
  val runtime = Runtime.default

  val panel = new JPanel(BorderLayout()):
    add(inputScroll(input), BorderLayout.CENTER)

  def impl(
    elem: Elem,
    updateChan: Channel[Boolean],
    initialWords: String,
  ): Unit =
    input setText initialWords
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeWordsInput(elem, updateChan, input.getText)

  for
    elem  ← data
    chan  ← updateChannel()
    words ← wordsInput
    _     ← ZIO attempt impl(elem, chan, words)
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