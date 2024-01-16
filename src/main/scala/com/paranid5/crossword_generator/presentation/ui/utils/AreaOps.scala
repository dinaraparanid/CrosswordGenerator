package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.geom.Area
import scala.annotation.targetName

extension (area: Area)
  @targetName("add")
  def + (rhs: Area): Area =
    val self = Area(area)
    self add rhs
    self

  @targetName("intersect")
  def & (rhs: Area): Area =
    val self = Area(area)
    self intersect rhs
    self
