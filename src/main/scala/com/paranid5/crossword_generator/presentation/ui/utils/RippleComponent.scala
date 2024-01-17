package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.*
import javax.swing.JComponent

/**
 * [[RoundedComponent]] with a ripple effect on click
 * Provides functionality to set color and radius
 */

trait RippleComponent extends RoundedComponent:
  private var _rippleRadius: Int = 10

  private val rippleEffect: RippleEffect =
    RippleEffect(this)

  /** Color of ripple effect */

  def rippleColor: Color =
    rippleEffect.rippleColor

  /** Radius of ripple effect */

  def rippleRadius: Int = _rippleRadius

  /**
   * Updates ripple effect color and repaints component
   * @param color new ripple effect color
   */

  def setRippleColor(color: Color): Unit =
    rippleEffect.rippleColor = color
    repaint()

  /**
   * Updates ripple effect radius and repaints component
   * @param radius new ripple effect radius
   */

  def setRippleRadius(radius: Int): Unit =
    _rippleRadius = radius
    repaint()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    rippleEffect.render(g, roundedArea)