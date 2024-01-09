package presentation.generation.input

import data.app.{AppConfig, InputStates, resetTitle}
import presentation.ui.Theme
import presentation.ui.utils.{PlaceholderTextComponent, combine, removeCaretListeners}
import presentation.{appFontStream, appThemeStream, inputStates}

import zio.{Runtime, URIO, Unsafe, ZIO}

import javax.swing.JTextField

private val TitlePlaceholder = "Crossword title"
private val TitleMaxWidth = 20

def TitleInput(): URIO[AppConfig & InputStates, JTextField] =
  val input = new JTextField
    with PlaceholderTextComponent:
    setPlaceholder(TitlePlaceholder)
    setColumns(TitleMaxWidth)

  val runtime = Runtime.default

  def recompose(
    theme:       Theme,
    typeface:    String,
    inputStates: InputStates
  ): Unit =
    input setBackground theme.backgroundColor.darker()
    input setForeground theme.fontColor
    input setPlaceholderColor theme.primaryColor
    input setFont labelFont(typeface)

    input.removeCaretListeners()
    input addCaretListener: _ ⇒
      Unsafe.unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          inputStates resetTitle input.getText
      }

  for {
    theme  ← appThemeStream()
    font   ← appFontStream()
    inputs ← inputStates()
    _      ← combine(theme, font).foreach { case (t, f) ⇒
      ZIO attempt recompose(t, f, inputs)
    }.fork
  } yield input
