package com.paranid5.crossword_generator.data.app.navigation

import zio.stream.SubscriptionRef
import zio.{UIO, ULayer, ZLayer}

import java.awt.CardLayout
import javax.swing.JPanel

/**
 * Service for managing navigation between screens
 * @param nav navigator between screens
 */

final class NavigationService(val nav: SubscriptionRef[Option[Navigator]]) extends AnyVal:
  /**
   * Invalidates the current navigation and sets
   * a new navigator based on the specified panel, card layout, and a frame
   *
   * @param panel holds and maps all managed screens
   * @param card navigator itself
   * @return a UIO that will complete
   *         when the navigator has been updated
   */

  def invalidate(panel: JPanel, card: CardLayout): UIO[Unit] =
    nav set Option(Navigator(panel, card))

/** Provides a layer for the [[NavigationService]] */

object NavigationService:
  /**
   * Creates a ZIO layer for the NavigationService
   * @return a [[ULayer]] that provides the [[NavigationService]]
   */

  lazy val layer: ULayer[NavigationService] = ZLayer:
    SubscriptionRef make Option.empty[Navigator] map (NavigationService(_))