package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.{Color, Font, Graphics, Graphics2D}
import javax.swing.text.JTextComponent

/**
 * [[JTextComponent]] with a placeholder text.
 * Text is shown when no input text is entered.
 * Provides functionality to set the text,
 * its color and font
 */

trait PlaceholderTextComponent extends JTextComponent:
  private var _placeholder: String = ""
  private var _placeholderColor: Color = getDisabledTextColor
  private var _placeholderFont: Font = getFont

  /** Placeholder text */
  def placeholder: String = _placeholder

  /** Placeholder color */
  def placeholderColor: Color = _placeholderColor

  /** Placeholder font */
  def placeholderFont: Font = _placeholderFont

  /**
   * Updates placeholder text and repaints component
   * @param text new placeholder text
   */

  def setPlaceholder(text: String): Unit =
    _placeholder = text
    repaint()

  /**
   * Updates placeholder text color and repaints component
   * @param color new placeholder text color
   */

  def setPlaceholderColor(color: Color): Unit =
    _placeholderColor = color
    repaint()

  /**
   * Updates placeholder text font and repaints component
   * @param font new placeholder text font
   */

  def setPlaceholderFont(font: Font): Unit =
    _placeholderFont = font
    repaint()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)

    if _placeholder.isBlank || !getText.isBlank then
      return

    val graphics = g.create().asInstanceOf[Graphics2D]
    graphics.setQualityRenderingHints()
    graphics setColor _placeholderColor

    val lineHeight = graphics
      .getFontMetrics(_placeholderFont)
      .getMaxAscent

    _placeholder
      .split("\n")
      .zipWithIndex
      .foreach:
        case (s, i) ⇒
          graphics.drawString(
            s,
            getInsets.left,
            lineHeight * (i + 1) + getInsets.top,
          )
