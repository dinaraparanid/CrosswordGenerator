package presentation.ui.utils

import java.awt.{Color, Font, Graphics, Graphics2D}
import javax.swing.text.JTextComponent

trait PlaceholderTextComponent extends JTextComponent:
  private var _placeholder: String = ""
  private var _placeholderColor: Color = getDisabledTextColor
  private var _placeholderFont: Font = getFont

  def placeholder: String = _placeholder
  def placeholderColor: Color = _placeholderColor
  def placeholderFont: Font = _placeholderFont

  def setPlaceholder(text: String): Unit =
    _placeholder = text
    repaint()

  def setPlaceholderColor(color: Color): Unit =
    _placeholderColor = color
    repaint()

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
