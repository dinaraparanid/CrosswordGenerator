package presentation.main

import data.app.AppConfig
import presentation.appTheme
import presentation.ui.Theme
import zio.ZIO

import java.awt.Font
import javax.swing.JLabel
import javax.swing.border.EmptyBorder

def AppTitle(): ZIO[AppConfig, Nothing, JLabel] =
  def impl(theme: Theme): JLabel =
    new JLabel("Crossword Generator"):
      setForeground(theme.secondaryColor)
      setBorder(labelBorder)
      setFont(labelFont)

  for {
    theme ← appTheme()
  } yield impl(theme)

private def labelFont: Font =
  Font("Pristina", Font.BOLD, 50)

private def labelBorder: EmptyBorder =
  EmptyBorder(10, 10, 10, 10)