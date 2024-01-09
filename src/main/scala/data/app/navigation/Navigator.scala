package data.app.navigation

import java.awt.CardLayout
import javax.swing.{JFrame, JPanel}

final class Navigator(
  private val panel: JPanel,
  private val card: CardLayout,
  val frame: JFrame
):
  def navigateTo(location: String): Unit =
    card.show(panel, location)

  def navigateToMainScreen(): Unit =
    navigateTo(Navigator.MainScreenNav)

  def navigateToGenerateScreen(): Unit =
    navigateTo(Navigator.GenerateScreenNav)

  def navigateToSettingsScreen(): Unit =
    navigateTo(Navigator.SettingsScreenNav)

object Navigator:
  val MainScreenNav = "main_screen"
  val GenerateScreenNav = "generate_screen"
  val SettingsScreenNav = "settings_screen"
