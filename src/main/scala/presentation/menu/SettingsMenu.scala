package presentation.menu

import data.app.navigation.NavigationService
import data.app.AppConfig
import presentation.{appConfig, navigator}
import presentation.ui.utils.ctrlKey

import zio.{RIO, URIO, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem}

def SettingsMenu(): RIO[AppConfig & NavigationService, JMenu] =
  val menu = new JMenu:
    add(GenerationMenuItem())

  def setContentOfMenu(appearanceItem: JMenuItem): Unit =
    menu add appearanceItem

  for {
    appearanceItem ← AppearanceMenuItem()
    _              ← ZIO attempt
      setContentOfMenu(appearanceItem)
  } yield menu

private def AppearanceMenuItem(): URIO[AppConfig & NavigationService, JMenuItem] =
  for {
    conf ← appConfig()
    nav  ← navigator()
  } yield new JMenuItem("Appearance"):
    setAccelerator(ctrlKey(KeyEvent.VK_U))
    addActionListener { _ ⇒
      nav foreach { n ⇒ conf.resetTheme(n.frame) }
    }

private def GenerationMenuItem(): JMenuItem =
  new JMenuItem("Generation"):
    setAccelerator(ctrlKey(KeyEvent.VK_G))
    addActionListener { _ ⇒ println("TODO: Generation") }
