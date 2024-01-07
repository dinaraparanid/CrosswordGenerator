package presentation

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlIJTheme
import data.app.AppConfig
import data.app.navigation.{NavigationService, Navigator}
import presentation.generation.GenerationScreen
import presentation.main.MainScreen
import presentation.settings.SettingsScreen
import zio.ZIO

import java.awt.{Font, GraphicsEnvironment}
import java.io.File
import javax.swing.{JFrame, JPanel, WindowConstants}

def MainFrame(): ZIO[AppConfig & NavigationService, Throwable, JFrame] =
  setup()

  def impl(navPanel: JPanel): JFrame =
    new JFrame("Crossword Generator"):
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      setBounds(0, 0, 600, 500)
      add(navPanel)
      setLocationRelativeTo(null)

  val (panel, card) = NavigationPanel()

  for {
    navService ← navigationService()
    _ ← navService.invalidate(panel, card)

    mainScreen ← MainScreen()
    generationScreen ← GenerationScreen()
    settingsScreen ← SettingsScreen()

    _ ← ZIO.attempt(panel.add(mainScreen, Navigator.MainScreenNav))
    _ ← ZIO.attempt(panel.add(generationScreen, Navigator.GenerateScreenNav))
    _ ← ZIO.attempt(panel.add(settingsScreen, Navigator.SettingsScreenNav))
  } yield impl(panel)

private def setup(): Unit =
  FlatNightOwlIJTheme.setup()
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, File("./res/pristina.ttf")))
