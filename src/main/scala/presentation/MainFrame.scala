package presentation

import data.FullEnvironment
import data.app.{AppBroadcast, SessionBroadcast}
import data.app.navigation.NavigationService
import presentation.menu.MainMenuBar

import zio.{RIO, Scope, ZIO}

import java.awt.Frame
import javax.swing.*

def MainFrame(): RIO[FullEnvironment & Scope, JFrame] =
  val frame = new JFrame("Crossword Generator"):
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    setExtendedState(Frame.MAXIMIZED_BOTH)
    setLocationRelativeTo(null)

  def setContentOfFrame(navPanel: JPanel, mainMenu: JMenuBar): Unit =
    frame add navPanel
    frame setJMenuBar mainMenu

  for
    cardPanel     ← NavigationPanel()
    (card, panel) = cardPanel
    
    app ← appBroadcast()
    _   ← app.startThemeMonitoring(frame).fork

    navService ← navigationService()
    _          ← navService.invalidate(panel, card, frame)

    mainMenu ← MainMenuBar()
    _        ← ZIO attempt setContentOfFrame(panel, mainMenu)
  yield frame
