package presentation.generation.input

import data.app.{AppConfig, InputStates, resetWords}
import presentation.ui.Theme
import presentation.ui.utils.{PlaceholderTextComponent, combine, removeCaretListeners}
import presentation.{appFontStream, appThemeStream, inputStates}

import zio.{Runtime, URIO, Unsafe, ZIO}

import java.awt.BorderLayout
import javax.swing.{JPanel, JScrollPane, JTextArea}

private val WordsMaxWidth = 20
private val WordsMaxHeight = 16

private val WordsPlaceholder = "Type or paste your words here"

private val WordsInitialText =
  """Type or paste your words here
    |
    |Example:
    |Chieftain - the leader of the Cossacks.
    |Minotaur - Cretan monster with the body of a man and the head of a bull, who lived in a Labyrinth and was killed by Theseus.
    |Scimitar - bladed stabbing and slashing edged weapon with a long single-edged blade having a double bend; something between a saber and a cleaver.""".stripMargin

def WordsInput(): URIO[AppConfig & InputStates, JPanel] =
  val panel = JPanel(BorderLayout())

  val input = new JTextArea(WordsMaxHeight, WordsMaxWidth)
    with PlaceholderTextComponent:
    setText(WordsInitialText)
    setPlaceholder(WordsPlaceholder)
    setWrapStyleWord(true)
    setLineWrap(true)

  val scroll = JScrollPane(input)
  panel.add(scroll, BorderLayout.CENTER)

  val runtime = Runtime.default

  def recompose(
    theme:       Theme,
    typeface:    String,
    inputStates: InputStates
  ): Unit =
    scroll.getVerticalScrollBar setBackground theme.primaryColor

    input setBackground theme.backgroundColor.darker()
    input setForeground theme.fontColor
    input setCaretColor theme.primaryColor
    input setSelectionColor theme.primaryColor
    input setPlaceholderColor theme.primaryColor.darker()

    input setFont labelFont(typeface)

    input.removeCaretListeners()
    input addCaretListener: _ ⇒
      Unsafe.unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          inputStates resetWords input.getText
      }

  for {
    theme  ← appThemeStream()
    font   ← appFontStream()
    inputs ← inputStates()

    _ ← combine(theme, font).foreach { case (t, f) ⇒
      ZIO attempt recompose(t, f, inputs)
    }.fork
  } yield panel
