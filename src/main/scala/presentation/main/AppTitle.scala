package presentation.main

import data.app.AppConfig
import presentation.appThemeStream
import presentation.ui.Theme

import zio.{URIO, ZIO}

import java.awt.Font
import javax.swing.JLabel
import javax.swing.border.EmptyBorder

def AppTitle(): URIO[AppConfig, JLabel] =
  val title = new JLabel("Crossword Generator"):
    setBorder(labelBorder)
    setFont(labelFont)

  def recompose(theme: Theme): Unit =
    title.setForeground(theme.secondaryColor)

  for {
    themes ← appThemeStream()
    _ ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield title

private def labelFont: Font =
  Font("Pristina", Font.BOLD, 50)

private def labelBorder: EmptyBorder =
  EmptyBorder(10, 10, 10, 10)