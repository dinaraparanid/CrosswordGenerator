package com.paranid5.crossword_generator.presentation.generation.input

import com.paranid5.crossword_generator.data.storage.{StoragePreferences, dataStream, storageLock, storeTitleInput, titleInput, updateChannel}
import com.paranid5.crossword_generator.presentation.ui.utils.{PlaceholderTextComponent, removeCaretListeners}

import zio.channel.Channel
import zio.stm.TReentrantLock
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
  def recompose(
    updateChan:  Channel[Boolean],
    storageLock: TReentrantLock,
    elem:        Elem,
  ): Unit =
    input.removeCaretListeners()
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeTitleInput(elem, updateChan, storageLock, input.getText)

  for
    title ← titleInput
    _     ← ZIO attempt impl(title)

    elems ← dataStream
    chan  ← updateChannel()
    lock  ← storageLock
    _     ← elems
      .foreach:
        ZIO attempt recompose(chan, lock, _)
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