package presentation.main.menu

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme

import zio.{URIO, ZIO}

import java.awt.Toolkit
import java.awt.event.{ActionEvent, InputEvent}
import javax.swing.{JMenuItem, KeyStroke}

private def DefaultMenuItem(text: String, accelerator: KeyStroke)(
  onClick: ActionEvent ⇒ Unit
): URIO[AppConfig, JMenuItem] =
  val item = new JMenuItem(text):
    setOpaque(true)
    setAccelerator(accelerator)
    addActionListener(onClick(_))

  def impl(theme: Theme): Unit =
    item.setBackground(theme.backgroundColor.darker())
    item.setForeground(theme.fontColor)

  for {
    themes ← appThemeStream()
    _      ← themes.foreach(ZIO attempt impl(_)).fork
  } yield item

private def DefaultMenuItem(text: String)(
  onClick: ActionEvent ⇒ Unit
): URIO[AppConfig, JMenuItem] =
  val item = new JMenuItem(text):
    setOpaque(true)
    addActionListener(onClick(_))

  def recompose(theme: Theme): Unit =
    item setBackground theme.backgroundColor.darker()
    item setForeground theme.fontColor

  for {
    themes ← appThemeStream()
    _      ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield item

private def ctrlKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(
    key,
    Toolkit
      .getDefaultToolkit
      .getMenuShortcutKeyMaskEx
  )

private def altKey(key: Int): KeyStroke =
  KeyStroke.getKeyStroke(key, InputEvent.ALT_DOWN_MASK)