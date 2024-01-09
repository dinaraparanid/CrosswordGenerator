package presentation.main.menu

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme

import zio.{RIO, Unsafe, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem, JSeparator}

def FileMenu(): RIO[AppConfig, JMenu] =
  def impl(
    newMenuItem:  JMenuItem,
    openMenuItem: JMenuItem,
    exitMenuItem: JMenuItem,
    separator:    JSeparator,
  ): JMenu =
    new JMenu("File"):
      setOpaque(true)
      add(newMenuItem)
      add(openMenuItem)
      add(separator)
      add(exitMenuItem)

  def recompose(menu: JMenu, theme: Theme): Unit =
    menu setBackground theme.backgroundColor.darker()
    menu setForeground theme.fontColor

  for {
    newItem     ← NewMenuItem()
    openItem    ← OpenMenuItem()
    exitItem    ← ExitMenuItem()
    separator   ← DefaultSeparator()
    themes      ← appThemeStream()

    menu = impl(newItem, openItem, exitItem, separator)
    _    ← themes.foreach(ZIO attempt recompose(menu, _)).fork
  } yield menu

private def NewMenuItem(): RIO[AppConfig, JMenuItem] =
  DefaultMenuItem(
    text = "New",
    accelerator = ctrlKey(KeyEvent.VK_N)
  ) { _ ⇒ println("TODO: New") }

private def OpenMenuItem(): RIO[AppConfig, JMenuItem] =
  DefaultMenuItem(
    text = "Open",
    accelerator = ctrlKey(KeyEvent.VK_O)
  ) { _ ⇒ println("TODO: Open") }

private def ExitMenuItem(): RIO[AppConfig, JMenuItem] =
  DefaultMenuItem(
    text = "Exit",
    accelerator = altKey(KeyEvent.VK_F4)
  ): _ ⇒
    sys.exit(0)
