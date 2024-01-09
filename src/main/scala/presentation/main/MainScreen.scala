package presentation.main

import data.app.AppConfig
import data.app.navigation.NavigationService
import presentation.appThemeStream
import presentation.ui.Theme
import presentation.ui.utils.combine

import zio.{RIO, ZIO}

import java.awt.{Component, Dimension, GridBagLayout}
import javax.swing.*

def MainScreen(): RIO[AppConfig & NavigationService, JPanel] =
  val subPanel = new JPanel:
    setLayout(BoxLayout(this, BoxLayout.Y_AXIS))

  val mainPanel = new JPanel:
    setLayout(GridBagLayout())
    add(subPanel)

  def recomposeMainPanel(theme: Theme): Unit =
    mainPanel setBackground theme.backgroundColor

  def setContentOfSubPanel(
    appTitle:       JLabel,
    generateButton: JButton,
    settingsButton: JButton,
  ): Unit =
    appTitle setAlignmentX Component.CENTER_ALIGNMENT
    subPanel add appTitle

    subPanel add Box.createRigidArea(Dimension(0, 75))

    generateButton setAlignmentX Component.CENTER_ALIGNMENT
    subPanel add generateButton

    subPanel add Box.createRigidArea(Dimension(0, 30))

    settingsButton setAlignmentX Component.CENTER_ALIGNMENT
    subPanel add settingsButton

  def recomposeSubPanel(theme: Theme): Unit =
    subPanel setBackground theme.backgroundColor

  for {
    themes          ← appThemeStream()
    appTitles       ← AppTitle()
    generateButtons ← GenerateButton()
    settingsButtons ← SettingsButton()

    _ ← ZIO attempt setContentOfSubPanel(appTitles, generateButtons, settingsButtons)
    _ ← themes.foreach { theme ⇒
      ZIO attempt:
        recomposeSubPanel(theme)
        recomposeMainPanel(theme)
    }.fork
  } yield mainPanel
