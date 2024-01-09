package presentation

import data.app.{AppConfig, InputStates}
import data.app.navigation.{NavigationService, Navigator}
import presentation.generation.GenerationScreen
import presentation.main.MainScreen
import presentation.settings.SettingsScreen
import presentation.ui.utils.combine

import zio.{RIO, ZIO}

import java.awt.CardLayout
import javax.swing.JPanel

def NavigationPanel(): RIO[AppConfig & NavigationService & InputStates, (CardLayout, JPanel)] =
  val card = CardLayout()
  val panel = JPanel(card)

  def setContentOfPanel(
    mainScreen:       JPanel,
    settingsScreen:   JPanel,
    generationScreen: JPanel
  ): Unit =
    panel.add(mainScreen, Navigator.MainScreenNav)
    panel.add(generationScreen, Navigator.GenerateScreenNav)
    panel.add(settingsScreen, Navigator.SettingsScreenNav)

  for {
    mainScreen       ← MainScreen()
    generationScreen ← GenerationScreen()
    settingsScreen   ← SettingsScreen()

    _ ← ZIO attempt setContentOfPanel(
      mainScreen = mainScreen,
      generationScreen = generationScreen,
      settingsScreen = settingsScreen
    )
  } yield (card, panel)
