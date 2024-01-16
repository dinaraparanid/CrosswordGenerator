package com.paranid5.crossword_generator.data.app.navigation

import java.awt.CardLayout
import javax.swing.{JFrame, JPanel}

final class Navigator(
  private val panel: JPanel,
  private val card: CardLayout,
  val frame: JFrame
):
  private def navigateTo(location: String): Unit =
    card.show(panel, location)

  def navigateToGenerateScreen(): Unit =
    navigateTo(Navigator.GenerateScreenNav)

object Navigator:
  val GenerateScreenNav: String = "generate_screen"
