package presentation.main_menu

import data.app.AppConfig
import presentation.ui.utils.RippleComponent
import presentation.ui.{Theme, appTheme}
import zio.ZIO

import java.awt.Font
import javax.swing.JButton
import javax.swing.border.EmptyBorder

def GenerateButton(): ZIO[AppConfig, Nothing, JButton] =
  def impl(theme: Theme): JButton =
    new JButton("Generate") with RippleComponent:
      setBackground(theme.primaryColor)
      setForeground(theme.fontColor)
      setBorder(EmptyBorder(10, 12, 15, 12))
      setFont(buttonFont)
      setRadius(radius = 30)
      setRippleColor(theme.secondaryAlternativeColor)
      setFocusPainted(false)
      setBorderPainted(false)
      setContentAreaFilled(false)

  for {
    theme ← appTheme()
  } yield impl(theme)

private def buttonFont: Font =
  Font(Font.SERIF, Font.PLAIN, 20)