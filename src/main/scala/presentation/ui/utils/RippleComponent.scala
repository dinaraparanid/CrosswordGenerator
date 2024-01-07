package presentation.ui.utils

import java.awt.*
import javax.swing.JComponent

trait RippleComponent extends RoundedComponent:
  private var _rippleRadius: Int = 10

  private val rippleEffect: RippleEffect =
    RippleEffect(this)

  setRippleColor(getBackground)

  def rippleColor: Color =
    rippleEffect.rippleColor

  def rippleRadius: Int = _rippleRadius

  def setRippleColor(color: Color): Unit =
    rippleEffect.rippleColor = color
    repaint()

  def setRippleRadius(radius: Int): Unit =
    _rippleRadius = radius
    repaint()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    rippleEffect.render(g, roundArea)