package presentation.main

import data.app.AppConfig
import data.app.navigation.NavigationService
import presentation.appTheme
import presentation.ui.Theme
import zio.ZIO

import java.awt.{Component, Dimension, GridBagLayout}
import javax.swing.*

def MainScreen(): ZIO[AppConfig & NavigationService, Nothing, JPanel] =
  def impl(
    theme: Theme,
    appTitle: JLabel,
    generateButton: JButton,
    settingsButton: JButton,
  ): JPanel =
    new JPanel:
      setBackground(theme.backgroundColor)
      setLayout(GridBagLayout())
      add(content(theme, appTitle, generateButton, settingsButton))

  def content(
    theme:          Theme,
    appTitle:       JLabel,
    generateButton: JButton,
    settingsButton: JButton,
  ): JPanel =
    new JPanel:
      setBackground(theme.backgroundColor)
      setLayout(BoxLayout(this, BoxLayout.Y_AXIS))

      appTitle setAlignmentX Component.CENTER_ALIGNMENT
      add(appTitle)

      add(Box.createRigidArea(Dimension(0, 75)))

      generateButton setAlignmentX Component.CENTER_ALIGNMENT
      add(generateButton)

      add(Box.createRigidArea(Dimension(0, 30)))

      settingsButton setAlignmentX Component.CENTER_ALIGNMENT
      add(settingsButton)

  for {
    theme ← appTheme()
    appTitle ← AppTitle()
    generateButton ← GenerateButton()
    settingsButton ← SettingsButton()
  } yield impl(
    theme = theme,
    appTitle = appTitle,
    generateButton = generateButton,
    settingsButton = settingsButton
  )
