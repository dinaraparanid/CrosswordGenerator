package com.paranid5.crossword_generator.presentation.generation.input

import com.paranid5.crossword_generator.data.storage.{dataStream, updateChannel}
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, storeTitleInput, titleInput}
import com.paranid5.crossword_generator.presentation.ui.utils.PlaceholderTextComponent

import zio.channel.Channel
import zio.{RIO, Runtime, Unsafe, ZIO}

import javax.swing.JTextField

import scala.xml.Elem

private val TitlePlaceholder = "Crossword title"

/**
 * Composes text field for the title input
 * @return [[RIO]] with [[JTextField]] that completes
 *         when all required content is set
 */

def TitleInput(): RIO[StoragePreferences, JTextField] =
  val input = initialTitleInput
  val runtime = Runtime.default

  @inline
  def impl(initialText: String): Unit =
    input setText initialText

  @inline
  def recompose(updateChan: Channel[Boolean], elem: Elem): Unit =
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeTitleInput(elem, updateChan, input.getText)

  for
    title ← titleInput
    _     ← ZIO attempt impl(title)

    elems ← dataStream
    chan  ← updateChannel()
    _     ← elems
      .foreach:
        ZIO attempt recompose(chan, _)
      .fork
  yield input

/**
 * Composes text field with placeholder [[TitlePlaceholder]]
 * @return [[JTextField]] that is [[PlaceholderTextComponent]]
 */

@inline
private def initialTitleInput: JTextField =
  new JTextField
    with PlaceholderTextComponent:
    setPlaceholder(TitlePlaceholder)