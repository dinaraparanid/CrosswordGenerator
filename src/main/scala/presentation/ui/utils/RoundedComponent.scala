package presentation.ui.utils

import java.awt.geom.{Area, Rectangle2D, RoundRectangle2D}
import java.awt.{Graphics, Graphics2D}
import javax.swing.JComponent

trait RoundedComponent extends JComponent:
  setOpaque(false)

  private var _topLeftRadius: Int = 0
  private var _topRightRadius: Int = 0
  private var _bottomLeftRadius: Int = 0
  private var _bottomRightRadius: Int = 0

  def topLeftRadius: Int = _topLeftRadius
  def topRightRadius: Int = _topRightRadius
  def bottomLeftRadius: Int = _bottomLeftRadius
  def bottomRightRadius: Int = _bottomRightRadius

  def setTopLeftRadius(radius: Int): Unit =
    _topLeftRadius = radius
    repaint()

  def setTopRightRadius(radius: Int): Unit =
    _topRightRadius = radius
    repaint()

  def setBottomLeftRadius(radius: Int): Unit =
    _bottomLeftRadius = radius
    repaint()

  def setBottomRightRadius(radius: Int): Unit =
    _bottomRightRadius = radius
    repaint()

  def setRadius(radius: Int): Unit =
    setTopLeftRadius(radius)
    setTopRightRadius(radius)
    setBottomLeftRadius(radius)
    setBottomRightRadius(radius)

  override def paintComponent(g: Graphics): Unit =
    val g2 = g.create().asInstanceOf[Graphics2D]
    g2.setQualityRenderingHints()
    g2 setColor getBackground
    g2 fill this.roundArea
    super.paintComponent(g)

  protected def roundArea: Area =
    var area = this.roundTopLeftArea

    if this.topRightRadius > 0 then
      area &= this.roundTopRightArea

    if this.bottomLeftRadius > 0 then
      area &= this.roundBottomLeftArea

    if this.bottomRightRadius > 0 then
      area &= this.roundBottomRightArea

    area

extension (component: RoundedComponent)
  private def roundTopLeftArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.topLeftRadius)
    val roundY = math.min(height, component.topLeftRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(roundX / 2, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, roundY / 2, width - roundX / 2, height - roundY / 2))
    area

  private def roundTopRightArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.topRightRadius)
    val roundY = math.min(height, component.topRightRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(0, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, roundY / 2, width - roundX / 2, height - roundY / 2))
    area

  private def roundBottomLeftArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.bottomLeftRadius)
    val roundY = math.min(height, component.bottomLeftRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(roundX / 2, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, 0, width, height - roundY / 2))
    area

  private def roundBottomRightArea: Area =
    val width = component.getWidth
    val height = component.getHeight

    val roundX = math.min(width, component.bottomRightRadius)
    val roundY = math.min(height, component.bottomRightRadius)

    var area = Area(RoundRectangle2D.Double(0, 0, width, height, roundX, roundY))
    area += Area(Rectangle2D.Double(0, 0, width - roundX / 2, height))
    area += Area(Rectangle2D.Double(0, 0, width, height - roundY / 2))
    area
