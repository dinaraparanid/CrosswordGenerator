package presentation.main_menu

import data.app.AppConfig
import presentation.ui.utils.RoundedBorder
import presentation.ui.{Theme, appTheme}
import zio.ZIO

import java.awt.Font
import javax.swing.JButton

def GenerateButton(): ZIO[AppConfig, Nothing, JButton] =
  def impl(theme: Theme): JButton =
    new JButton("Generate") {
      setBackground(theme.backgroundColor)
      setForeground(theme.fontColor)
      setFont(buttonFont)
      setBorder(border(theme))
      setOpaque(true)
      setFocusPainted(false)
    }

  def buttonFont: Font =
    Font(Font.SERIF, Font.PLAIN, 18)

  def border(theme: Theme): RoundedBorder =
    RoundedBorder(
      radius = 30,
      primaryColor = theme.primaryColor,
    )

  for {
    theme ← appTheme()
  } yield impl(theme)
