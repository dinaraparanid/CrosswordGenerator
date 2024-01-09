package presentation.main

import data.app.AppConfig
import data.app.navigation.{NavigationService, Navigator}
import presentation.{appFontStream, appThemeStream, navigatorStream}
import presentation.ui.Theme
import presentation.ui.utils.RippleComponent
import presentation.ui.utils.combine

import zio.{ZIO, URIO}

import java.awt.Font
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton
import javax.swing.border.EmptyBorder

def MainMenuButton(text: String)(
  onClicked: (Option[Navigator], ActionEvent) ⇒ Unit
): URIO[AppConfig & NavigationService, JButton] =
  val button = new JButton(text) with RippleComponent:
    setBorder(EmptyBorder(10, 20, 15, 20))
    setRadius(radius = 30)
    setFocusPainted(false)
    setBorderPainted(false)
    setContentAreaFilled(false)

  def recompose(
    theme:     Theme,
    typeface:  String,
    navigator: Option[Navigator]
  ): Unit =
    button setBackground theme.primaryColor
    button setForeground theme.fontColor
    button setFont buttonFont(typeface)
    button setRippleColor theme.secondaryAlternativeColor
    button.removeActionListeners()
    button addActionListener (onClicked(navigator, _))

  for {
    themes ← appThemeStream()
    fonts  ← appFontStream()
    navs   ← navigatorStream()

    _ ← combine(themes, fonts, navs).foreach { case (t, f, n) ⇒
      ZIO attempt recompose(t, f, n)
    }.fork
  } yield button

private def buttonFont(font: String): Font =
  Font(font, Font.PLAIN, 26)

extension (button: JButton)
  def removeActionListeners(): Unit =
    button
      .getActionListeners
      .foreach(button.removeActionListener)
