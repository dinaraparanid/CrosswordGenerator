package presentation.generation.input

import data.app.{AppConfig, SessionStates, resetTitle}
import presentation.sessionStates
import presentation.ui.utils.PlaceholderTextComponent

import zio.{RIO, Runtime, Unsafe, ZIO}

import javax.swing.JTextField

private val TitlePlaceholder = "Crossword title"

def TitleInput(): RIO[AppConfig & SessionStates, JTextField] =
  val input = initialInputField
  val runtime = Runtime.default

  def setCaretListener(inputStates: SessionStates): Unit =
    input addCaretListener: _ ⇒
      Unsafe unsafe:
        implicit unsafe ⇒
          runtime.unsafe.runToFuture: 
            inputStates resetTitle input.getText

  for
    inputs ← sessionStates()
    _      ← ZIO attempt setCaretListener(inputs)
  yield input

private def initialInputField: JTextField =
  new JTextField
    with PlaceholderTextComponent:
    setPlaceholder(TitlePlaceholder)