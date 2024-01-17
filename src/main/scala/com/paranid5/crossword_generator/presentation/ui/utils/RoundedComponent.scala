package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.geom.{Area, Rectangle2D, RoundRectangle2D}
import java.awt.{AlphaComposite, BasicStroke, Color, Graphics, Graphics2D, Stroke}
import javax.swing.JComponent

/**
 * [[JComponent]] with a rounded borders
 * Provides functionality to set radius for all 4 edges,
 * border colors and paint stroke
 */

trait RoundedComponent extends JComponent:
  setOpaque(false)

  private var _topLeftRadius:     Int = 0
  private var _topRightRadius:    Int = 0
  private var _bottomLeftRadius:  Int = 0
  private var _bottomRightRadius: Int = 0

  private var _roundedBordersDrawn:    Boolean = false
  private var _roundedComponentColor:  Color   = getForeground
  private var _roundedComponentStroke: Stroke  = defaultStroke

  /** Top left corner radius */
  def topLeftRadius: Int = _topLeftRadius

  /** Top right corner radius */
  def topRightRadius: Int = _topRightRadius

  /** Bottom left corner radius */
  def bottomLeftRadius: Int = _bottomLeftRadius

  /** Bottom right corner radius */
  def bottomRightRadius: Int = _bottomRightRadius

  /** Are corners rounded */
  def isRoundedBorderDrawn: Boolean = _roundedBordersDrawn

  /** Rounded component background color */
  def roundedComponentColor: Color = _roundedComponentColor

  /** Rounded border corner stroke */
  def roundedComponentStroke: Stroke = _roundedComponentStroke

  /**
   * Updates top left radius and repaints component
   * @param radius new top left radius
   */

  def setTopLeftRadius(radius: Int): Unit =
    _topLeftRadius = radius
    repaint()

  /**
   * Updates top right radius and repaints component
   * @param radius new top right radius
   */

  def setTopRightRadius(radius: Int): Unit =
    _topRightRadius = radius
    repaint()

  /**
   * Updates bottom left radius and repaints component
   * @param radius new bottom left radius
   */

  def setBottomLeftRadius(radius: Int): Unit =
    _bottomLeftRadius = radius
    repaint()

  /**
   * Updates bottom right radius and repaints component
   * @param radius new bottom right radius
   */

  def setBottomRightRadius(radius: Int): Unit =
    _bottomRightRadius = radius
    repaint()

  /**
   * Resets radiuses for all edges and repaints component
   * @param radius new component corners radius
   */

  def setRadius(radius: Int): Unit =
    _topLeftRadius     = radius
    _topRightRadius    = radius
    _bottomLeftRadius  = radius
    _bottomRightRadius = radius
    repaint()

  /**
   * Hides/shows corners' borders and repaints component
   * @param isDrawn are borders drawn
   */

  def setRoundedBorderDrawn(isDrawn: Boolean): Unit =
    _roundedBordersDrawn = isDrawn
    repaint()

  /**
   * Updates component background color and repaints component
   * @param color rounded component color
   */

  def setRoundedComponentColor(color: Color): Unit =
    _roundedComponentColor = color
    repaint()

  /**
   * Updates component painter stroke and repaints component
   * @param stroke rounded component painter's stroke
   */

  def setRoundedComponentStroke(stroke: Stroke): Unit =
    _roundedComponentStroke = stroke
    repaint()

  override def paintComponent(g: Graphics): Unit =
    val g2 = g.create().asInstanceOf[Graphics2D]
    g2.setQualityRenderingHints()
    g2 setColor getBackground
    g2 fill roundedArea

    if _roundedBordersDrawn then
      g2 setStroke _roundedComponentStroke
      g2 setColor _roundedComponentColor
      g2 setComposite AlphaComposite.SrcIn
      g2 setClip roundedArea
      g2 draw roundedArea

    g2.dispose()
    super.paintComponent(g)

  /**
   * Totally intersected area
   * with all rounded edges applied
   */

  protected def roundedArea: Area =
    var area = this.roundTopLeftArea

    if this.topRightRadius > 0 then
      area &= this.roundTopRightArea

    if this.bottomLeftRadius > 0 then
      area &= this.roundBottomLeftArea

    if this.bottomRightRadius > 0 then
      area &= this.roundBottomRightArea

    area

extension (component: RoundedComponent)
  /**
   * Composes area with only
   * top left edge rounding applied
   *
   * @return top left rounded area
   */

  private def roundTopLeftArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.topLeftRadius)
    val roundY = math.min(height, component.topLeftRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(roundX / 2, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, roundY / 2, width - roundX / 2, height - roundY / 2))
    area

  /**
   * Composes area with only
   * top right edge rounding applied
   *
   * @return top right rounded area
   */

  private def roundTopRightArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.topRightRadius)
    val roundY = math.min(height, component.topRightRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(0, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, roundY / 2, width - roundX / 2, height - roundY / 2))
    area

  /**
   * Composes area with only
   * bottom left edge rounding applied
   *
   * @return bottom left rounded area
   */

  private def roundBottomLeftArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.bottomLeftRadius)
    val roundY = math.min(height, component.bottomLeftRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(roundX / 2, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, 0, width, height - roundY / 2))
    area

  /**
   * Composes area with only
   * bottom right edge rounding applied
   *
   * @return bottom right rounded area
   */

  private def roundBottomRightArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.bottomRightRadius)
    val roundY = math.min(height, component.bottomRightRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(0, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, 0, width, height - roundY / 2))
    area

/** Default stroke for the rounded component */

private def defaultStroke: Stroke =
  BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)