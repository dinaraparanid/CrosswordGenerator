package presentation.main

import data.app.AppConfig
import presentation.{appFont, appTheme}
import presentation.ui.utils.RippleComponent
import presentation.ui.Theme
import zio.ZIO

import java.awt.Font
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton
import javax.swing.border.EmptyBorder

def MainMenuButton(text: String)(onClicked: ActionEvent ⇒ Unit): ZIO[AppConfig, Nothing, JButton] =
  def impl(theme: Theme, typeface: String): JButton =
    new JButton(text) with RippleComponent:
      setBackground(theme.primaryColor)
      setForeground(theme.fontColor)
      setBorder(EmptyBorder(10, 20, 15, 20))
      setFont(buttonFont(typeface))
      setRadius(radius = 30)
      setRippleColor(theme.secondaryAlternativeColor)
      setFocusPainted(false)
      setBorderPainted(false)
      setContentAreaFilled(false)
      addActionListener(onClicked(_))

  for {
    theme ← appTheme()
    font ← appFont()
  } yield impl(theme, font)

private def buttonFont(font: String): Font =
  Font(font, Font.PLAIN, 26)
