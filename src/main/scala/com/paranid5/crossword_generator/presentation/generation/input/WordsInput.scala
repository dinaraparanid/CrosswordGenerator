package com.paranid5.crossword_generator.presentation.generation.input

import com.paranid5.crossword_generator.data.storage.*
import com.paranid5.crossword_generator.presentation.ui.utils.PlaceholderTextComponent

import zio.channel.Channel
import zio.{RIO, Runtime, Unsafe, ZIO}

import java.awt.BorderLayout
import javax.swing.{JPanel, JScrollPane, JTextArea, ScrollPaneConstants}

import scala.xml.Elem

private val WordsPlaceholder = "Type or paste your words here"

/**
 * Composes [[JTextArea]] for the
 * words with their meanings input.
 * Provides [[JScrollPane]] for the text area
 *
 * @return [[RIO]] with [[JPanel]] that completes
 *         when all required content is set
 */

def WordsInput(): RIO[StoragePreferences, JPanel] =
  val input = initialInputArea
  val runtime = Runtime.default

  val panel = new JPanel(BorderLayout()):
    add(inputScroll(input), BorderLayout.CENTER)

  @inline
  def impl(initialWords: String): Unit =
    input setText initialWords

  @inline
  def recompose(updateChan: Channel[Boolean], elem: Elem): Unit =
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeWordsInput(elem, updateChan, input.getText)

  for
    words ← wordsInput
    _     ← ZIO attempt impl(words)

    elems ← dataStream
    chan  ← updateChannel()
    _     ← elems
      .foreach:
        ZIO attempt recompose(chan, _)
      .fork
  yield panel

/**
 * Composes text area with placeholder [[WordsPlaceholder]]
 * @return [[JTextArea]] that is [[PlaceholderTextComponent]]
 */

@inline
private def initialInputArea: JTextArea =
  new JTextArea
    with PlaceholderTextComponent:
    setPlaceholder(WordsPlaceholder)
    setWrapStyleWord(true)
    setLineWrap(true)

/**
 * Composes scroll pane for the words input area
 * @return [[JScrollPane]] that is shown vertically when required
 */

@inline
private def inputScroll(input: JTextArea): JScrollPane =
  new JScrollPane(input):
    setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED)
    setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)