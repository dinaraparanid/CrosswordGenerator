package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.geom.Area
import scala.annotation.targetName

extension (area: Area)
  /**
   * Adds the shape of the specified [[Area]] to the
   * shape of this [[Area]].
   * The resulting shape of this area will include
   * the union of both shapes, or all areas that were contained
   * in either this or the specified area
   *
   * <pre>
   *      a1           +         a2          =        a3
   *
   * ################     ################     ################
   * ##############         ##############     ################
   * ############             ############     ################
   * ##########                 ##########     ################
   * ########                     ########     ################
   * ######                         ######     ######    ######
   * ####                             ####     ####        ####
   * ##                                 ##     ##            ##
   * </pre>
   *
   * @param rhs the [[Area]] to be added to the current shape
   * @return Union shape from both areas
   */

  @targetName("add")
  def + (rhs: Area): Area =
    val self = Area(area)
    self add rhs
    self

  /**
   * Sets the shape of this [[Area]] to the intersection of
   * its current shape and the shape of the specified [[Area]].
   * The resulting shape of this area will include
   * only areas that were contained in both this area
   * and also in the specified area
   *
   * <pre>
   *      a1           &          a2        =         a3
   *
   * ################     ################     ################
   * ##############         ##############       ############
   * ############             ############         ########
   * ##########                 ##########           ####
   * ########                     ########
   * ######                         ######
   * ####                             ####
   * ##                                 ##
   * </pre>
   *
   * @param rhs the [[Area]] to be intersected with the current area
   * @return intersection area from both areas
   */

  @targetName("intersect")
  def & (rhs: Area): Area =
    val self = Area(area)
    self intersect rhs
    self
