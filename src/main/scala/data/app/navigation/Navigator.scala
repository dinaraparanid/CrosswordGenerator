package data.app.navigation

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
  val GenerateScreenNav = "generate_screen"
