package com.paranid5.crossword_generator.presentation.generation.input

import com.paranid5.crossword_generator.data.storage.{data, updateChannel}
import com.paranid5.crossword_generator.data.storage.{StoragePreferences, storeTitleInput, titleInput}
import com.paranid5.crossword_generator.presentation.ui.utils.PlaceholderTextComponent

import zio.channel.Channel
import zio.{RIO, Runtime, Scope, Unsafe, ZIO}

import javax.swing.JTextField
import scala.xml.Elem

private val TitlePlaceholder = "Crossword title"

def TitleInput(): RIO[StoragePreferences & Scope, JTextField] =
  val input = initialInputField
  val runtime = Runtime.default

  def impl(
    elem:        Elem,
    updateChan:  Channel[Boolean],
    initialText: String,
  ): Unit =
    input setText initialText
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture:
            storeTitleInput(elem, updateChan, input.getText)

  for
    elem  ← data
    chan  ← updateChannel()
    title ← titleInput
    _     ← ZIO attempt impl(elem, chan, title)
  yield input

private def initialInputField: JTextField =
  new JTextField
    with PlaceholderTextComponent:
    setPlaceholder(TitlePlaceholder)