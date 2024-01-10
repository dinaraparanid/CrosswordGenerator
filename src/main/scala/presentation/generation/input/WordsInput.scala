package presentation.generation.input

import data.app.{AppConfig, InputStates, resetWords}
import presentation.inputStates
import presentation.ui.utils.PlaceholderTextComponent

import zio.{RIO, Runtime, Unsafe, ZIO}

import java.awt.BorderLayout
import javax.swing.{JPanel, JScrollPane, JTextArea, ScrollPaneConstants}

private val WordsPlaceholder = "Type or paste your words here"

private val WordsInitialText =
  """Type or paste your words here
    |
    |Example:
    |Chieftain - the leader of the Cossacks.
    |Minotaur - Cretan monster with the body of a man and the head of a bull, who lived in a Labyrinth and was killed by Theseus.
    |Scimitar - bladed stabbing and slashing edged weapon with a long single-edged blade having a double bend; something between a saber and a cleaver.""".stripMargin

def WordsInput(): RIO[AppConfig & InputStates, JPanel] =
  val input = initialInputArea

  val panel = new JPanel(BorderLayout()):
    add(inputScroll(input), BorderLayout.CENTER)

  val runtime = Runtime.default

  def setCaretListener(inputStates: InputStates): Unit =
    input addCaretListener: _ ⇒
      Unsafe unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          inputStates resetWords input.getText
      }

  for {
    inputs ← inputStates()
    _      ← ZIO attempt setCaretListener(inputs)
  } yield panel

private def initialInputArea: JTextArea =
  new JTextArea
    with PlaceholderTextComponent:
    setText(WordsInitialText)
    setPlaceholder(WordsPlaceholder)
    setWrapStyleWord(true)
    setLineWrap(true)

private def inputScroll(input: JTextArea): JScrollPane =
  new JScrollPane(input):
    setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED)
    setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)