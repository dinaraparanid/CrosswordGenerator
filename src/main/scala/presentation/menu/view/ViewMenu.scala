package presentation.menu.view

import data.app.AppBroadcast
import data.app.navigation.NavigationService
import presentation.ui.utils.ctrlKey
import presentation.{appBroadcast, navigator}

import zio.{RIO, URIO, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem}

def ViewMenu(): RIO[AppBroadcast & NavigationService, JMenu] =
  val menu = JMenu("View")

  def setContentOfMenu(appearanceItem: JMenuItem): Unit =
    menu add appearanceItem
    menu add FontMenuItem()

  for
    appearanceItem ← ThemeMenuItem()
    _              ← ZIO attempt
      setContentOfMenu(appearanceItem)
  yield menu

private def ThemeMenuItem(): URIO[AppBroadcast & NavigationService, JMenuItem] =
  for app ← appBroadcast() 
    yield new JMenuItem("Theme"):
      setAccelerator(ctrlKey(KeyEvent.VK_U))
      addActionListener: _ ⇒
        app.resetTheme()

private def FontMenuItem(): JMenuItem =
  new JMenuItem("Font"):
    addActionListener { _ ⇒ println("Font") }
