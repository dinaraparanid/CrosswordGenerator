package presentation.generation.input

import presentation.inputStates
import data.app.{InputStates, isInputCorrectStream}

import zio.{URIO, ZIO}

import javax.swing.JButton

def GenerateButton(): URIO[InputStates, JButton] =
  val button = new JButton("Generate"):
    putClientProperty("JButton.buttonType", "roundRect")
    addActionListener { _ ⇒ println("TODO: Generate Crossword") }

  def recompose(isCrosswordCorrect: Boolean): Unit =
    button setEnabled isCrosswordCorrect

  for {
    inputs ← inputStates()
    _      ← inputs
      .isInputCorrectStream
      .foreach(ZIO attempt recompose(_))
      .fork
  } yield button
