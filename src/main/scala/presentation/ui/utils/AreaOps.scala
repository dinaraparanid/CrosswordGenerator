package presentation.ui.utils

import java.awt.geom.Area
import scala.annotation.targetName

extension (area: Area)
  @targetName("add")
  def + (rhs: Area): Area =
    area add rhs
    area

  @targetName("intersect")
  def & (rhs: Area): Area =
    area intersect rhs
    area
