package presentation.ui.utils

import java.awt.*
import java.awt.geom.RoundRectangle2D
import javax.swing.border.Border

class RoundedBorder(radius: Int, primaryColor: Color) extends Border {
  override def getBorderInsets(c: Component): Insets =
    Insets(radius + 1, radius + 1, radius + 1, radius + 1)

  override def isBorderOpaque: Boolean = true

  override def paintBorder(
    c:      Component,
    g:      Graphics,
    x:      Int,
    y:      Int,
    width:  Int,
    height: Int
  ): Unit = {
    val graphics = g.create().asInstanceOf[Graphics2D]
    val inner = RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius)

    graphics setColor primaryColor
    graphics draw inner
    graphics.dispose()
  }
}
