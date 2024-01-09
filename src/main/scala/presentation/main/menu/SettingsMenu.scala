package presentation.main.menu

import data.app.{AppConfig, resetTheme}
import presentation.{appConfig, appThemeStream}
import presentation.ui.Theme

import zio.{Runtime, URIO, Unsafe, ZIO}

import java.awt.event.KeyEvent
import javax.swing.{JMenu, JMenuItem}

def SettingsMenu(): URIO[AppConfig, JMenu] =
  def impl(
    appearanceItem: JMenuItem,
    generationItem: JMenuItem,
  ): JMenu =
    new JMenu("Settings"):
      setOpaque(true)
      add(appearanceItem)
      add(generationItem)

  def recompose(menu: JMenu, theme: Theme): Unit =
    menu setBackground theme.backgroundColor.darker()
    menu setForeground theme.fontColor

  for {
    appearanceItem ← AppearanceMenuItem()
    generationItem ← GenerationMenuItem()
    themes         ← appThemeStream()

    menu = impl(appearanceItem, generationItem)
    _    ← themes.foreach(ZIO attempt recompose(menu, _)).fork
  } yield menu

private def AppearanceMenuItem(): URIO[AppConfig, JMenuItem] =
  val runtime = Runtime.default

  for {
    conf ← appConfig()

    view ← DefaultMenuItem(
      text = "Appearance",
      accelerator = ctrlKey(KeyEvent.VK_A)
    ): _ ⇒
      Unsafe.unsafe { implicit unsafe ⇒
        runtime.unsafe.runToFuture:
          conf.resetTheme()
      }
  } yield view

private def GenerationMenuItem(): URIO[AppConfig, JMenuItem] =
  DefaultMenuItem(
    text = "Generation",
    accelerator = ctrlKey(KeyEvent.VK_G)
  ): _ ⇒
    println("TODO: Generation")
