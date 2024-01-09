package presentation.generation.input

import data.app.{AppConfig, InputStates}
import presentation.appThemeStream
import presentation.generation.initialConstraints
import presentation.ui.Theme
import zio.{RIO, ZIO}

import java.awt.{Dimension, GridBagLayout}
import javax.swing.*

def InputPanel(): RIO[AppConfig & InputStates, JPanel] = {
  val gbLayout = GridBagLayout()

  val panel = new JPanel:
    setLayout(gbLayout)

  val gbc = initialConstraints

  def setContentOfPanel(
    titleLabel: JLabel,
    titleInput: JTextField,
    wordsLabel: JLabel,
    wordsInput: JPanel,
  ): Unit =
    gbc.gridy = 0
    gbc.gridwidth = 20
    gbc.gridheight = 1
    panel.add(titleLabel, gbc)
    panel add Box.createRigidArea(Dimension(0, 5))

    gbc.gridy = 1
    gbc.gridwidth = 20
    gbc.gridheight = 1
    panel.add(titleInput, gbc)
    panel add Box.createRigidArea(Dimension(0, 20))

    gbc.gridy = 2
    gbc.gridwidth = 20
    gbc.gridheight = 1
    panel.add(wordsLabel, gbc)
    panel add Box.createRigidArea(Dimension(0, 5))

    gbc.gridy = 3
    gbc.gridwidth = 20
    gbc.gridheight = 16
    gbc.weightx = 1
    gbc.weighty = 1
    panel.add(wordsInput, gbc)

  def recompose(theme: Theme): Unit =
    panel.setBackground(theme.backgroundColor)

  for {
    titleLabel ← TitleLabel()
    titleInput ← TitleInput()
    wordsLabel ← WordsLabel()
    wordsInput ← WordsInput()
    _          ← ZIO attempt
      setContentOfPanel(
        titleLabel = titleLabel,
        titleInput = titleInput,
        wordsLabel = wordsLabel,
        wordsInput = wordsInput
      )

    themes ← appThemeStream()
    _      ← themes.foreach(ZIO attempt recompose(_)).fork
  } yield panel
}

private def TitleLabel(): RIO[AppConfig, JLabel] =
  InputLabel("Title")

private def WordsLabel(): RIO[AppConfig, JLabel] =
  InputLabel("Words")
