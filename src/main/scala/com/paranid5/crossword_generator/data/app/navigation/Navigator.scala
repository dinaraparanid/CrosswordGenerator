package com.paranid5.crossword_generator.data.app.navigation

import java.awt.CardLayout
import javax.swing.{JFrame, JPanel}

/**
 * Provides a navigator for switching between screens
 *
 * @param panel holds and maps all managed screens
 * @param card navigator itself
 */

final class Navigator(
  private val panel: JPanel,
  private val card: CardLayout,
):
  /**
   * Navigates to the specified screen
   * @param location screen to navigate
   */

  private def navigateTo(location: String): Unit =
    card.show(panel, location)

  /** Navigates to the generation screen */

  def navigateToGenerateScreen(): Unit =
    navigateTo(Navigator.GenerateScreenNav)

/** Provides possible screens to navigate */

object Navigator:
  val GenerateScreenNav: String = "generate_screen"
