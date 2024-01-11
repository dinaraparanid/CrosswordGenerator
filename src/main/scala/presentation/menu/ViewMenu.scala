package presentation.menu

import data.app.AppConfig
import data.app.navigation.NavigationService
import presentation.ui.utils.ctrlKey
import presentation.{appConfig, navigator}

import zio.{RIO, URIO, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem}

def ViewMenu(): RIO[AppConfig & NavigationService, JMenu] =
  val menu = JMenu("View")

  def setContentOfMenu(appearanceItem: JMenuItem): Unit =
    menu add appearanceItem
    menu add FontMenuItem()

  for {
    appearanceItem ← ThemeMenuItem()
    _              ← ZIO attempt
      setContentOfMenu(appearanceItem)
  } yield menu

private def ThemeMenuItem(): URIO[AppConfig & NavigationService, JMenuItem] =
  for {
    conf ← appConfig()
    nav  ← navigator()
  } yield new JMenuItem("Theme"):
    setAccelerator(ctrlKey(KeyEvent.VK_U))
    addActionListener { _ ⇒
      nav foreach { n ⇒ conf.resetTheme(n.frame) }
    }

private def FontMenuItem(): JMenuItem =
  new JMenuItem("Font"):
    addActionListener { _ ⇒ println("Font") }
