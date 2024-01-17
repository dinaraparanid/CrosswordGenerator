package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import javax.swing.SwingUtilities

import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTargetAdapter

import scala.collection.mutable

/**
 * Provides ripple effect functionality
 * to the given [[Component]]
 *
 * @param component component to which ripple
 *                  effect has to be applied
 */

final class RippleEffect(component: Component):
  component addMouseListener
    new MouseAdapter():
      override def mousePressed(e: MouseEvent): Unit =
        if SwingUtilities isLeftMouseButton e then
          addEffect(e.getPoint)

  var rippleColor: Color =
    component.getBackground

  private val effects: mutable.Map[Long, RippleEffectImpl] =
    mutable.HashMap[Long, RippleEffectImpl]()

  private def addEffect(location: Point): Unit =
    val effect = RippleEffectImpl(component, location, rippleColor, effects)
    effects(effect.id) = effect

  /**
   * Applies all ripple effects to the component
   *
   * @param g component graphics
   * @param shape component shape
   */

  def render(g: Graphics, shape: Shape): Unit =
    val g2 = g.create.asInstanceOf[Graphics2D]
    g2.setQualityRenderingHints()
    effects foreach { case (_, effect) ⇒ effect.render(g2, shape) }

/**
 * Single ripple click effect implementation
 *
 * @param component   component to which ripple effect has to be applied
 * @param location    touch location
 * @param rippleColor color of ripple effect
 * @param effects     global map of all effects' IDs and effects themselves
 * @param id          ID of the current effect
 */

private final class RippleEffectImpl(
  private val component: Component,
  private val location: Point,
  private val rippleColor: Color,
  private val effects: mutable.Map[Long, RippleEffectImpl],
  val id: Long = System.currentTimeMillis(),
):
  private var animFraction = 0F
  private val animator = Animator(500, timingAdapter)

  animator setResolution 5
  animator setDeceleration 0.5F
  animator.start()

  /**
   * Applies ripple effect to the component
   *
   * @param g2    component 2D graphics
   * @param shape component shape
   * @param alpha effect alpha
   */

  def render(
    g2:    Graphics2D,
    shape: Shape,
    alpha: Float = 0.3F
  ): Unit =
    var _alpha = alpha

    if animFraction >= 0.7F then
      _alpha -= _alpha * ((animFraction - 0.7F) / 0.3F)

    g2 setColor rippleColor
    g2 setComposite AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _alpha)
    g2 fill Area(shape) & Area(getShape(getRadius(shape.getBounds2D)))

  /** Updates effect's ratio during the effect's animation */

  private def timingAdapter: TimingTargetAdapter =
    new TimingTargetAdapter:
      override def timingEvent(fraction: Float): Unit =
        animFraction = fraction
        component.repaint()

      override def end(): Unit =
        effects -= id

  /**
   * Provides shape of the effect as a rounded ellipse,
   * based on the animation fraction and the touch location
   *
   * @param size calculated size of the component
   * @return elliptic effect shape
   * @see [[getRadius]]
   */

  private def getShape(size: Double): Ellipse2D.Double =
    val s = size * animFraction
    val x = location.getX
    val y = location.getY
    Ellipse2D.Double(x - s, y - s, s * 2, s * 2)

  /**
   * Provides total radius of the effects' area
   *
   * @param rec components' bounds
   * @return total radius of the effect
   */

  private def getRadius(rec: Rectangle2D): Double =
    val size =
      if rec.getWidth > rec.getHeight then
        if location.getX < rec.getWidth / 2 then
          rec.getWidth - location.getX
        else
          location.getX
      else if location.getY < rec.getHeight / 2 then
        rec.getHeight - location.getY
      else
        location.getY

    size + size * 0.1F
