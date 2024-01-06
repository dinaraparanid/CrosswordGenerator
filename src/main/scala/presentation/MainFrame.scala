package presentation

import data.app.AppConfig
import presentation.main_menu.MainMenu
import zio.ZIO

import javax.swing.{JFrame, JPanel, WindowConstants}

def MainFrame(): ZIO[AppConfig, Nothing, JFrame] = {
  def impl(mainMenu: JPanel): JFrame = {
    new JFrame("Crossword Generator") {
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      setBounds(0, 0, 1900, 1000)
      add(mainMenu)
    }
  }

  for {
    mainMenu ← MainMenu()
  } yield impl(mainMenu)
}
