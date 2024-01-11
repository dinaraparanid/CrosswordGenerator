package presentation.generation.input

import data.app.{AppConfig, SessionStates}
import presentation.ui.utils.{VerticalSpacer, gbc}

import zio.{RIO, ZIO}

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.*

def InputPanel(): RIO[AppConfig & SessionStates, JPanel] =
  val gbLayout = GridBagLayout()

  val panel = new JPanel:
    setLayout(gbLayout)

  def setContentOfPanel(
    titleInput:     JTextField,
    wordsInput:     JPanel,
    generateButton: JButton
  ): Unit =
    panel.add(TitleLabel(), titleLabelGBC)
    panel.add(VerticalSpacer(height = 5), spacer1GBC)
    panel.add(titleInput, titleInputGBC)
    panel.add(VerticalSpacer(height = 20), spacer2GBC)
    panel.add(WordsLabel(), wordsLabelGBC)
    panel.add(VerticalSpacer(height = 5), spacer3GBC)
    panel.add(wordsInput, wordsInputGBC)
    panel.add(VerticalSpacer(height = 5), spacer4GBC)
    panel.add(generateButton, generateButtonGBC)

  for {
    titleInput     ← TitleInput()
    wordsInput     ← WordsInput()
    generateButton ← GenerateButton()

    _ ← ZIO attempt
      setContentOfPanel(
        titleInput = titleInput,
        wordsInput = wordsInput,
        generateButton = generateButton
      )
  } yield panel

private def TitleLabel(): JLabel = JLabel("Title")
private def WordsLabel(): JLabel = JLabel("Words")

private def titleLabelGBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 0,
    gridWidth = 20,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def spacer1GBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 1,
    gridWidth = 20,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def titleInputGBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 2,
    gridWidth = 20,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def spacer2GBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 3,
    gridWidth = 20,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def wordsLabelGBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 4,
    gridWidth = 20,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def spacer3GBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 5,
    gridWidth = 20,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def wordsInputGBC: GridBagConstraints =
  gbc(
    gridX = 0,
    gridY = 6,
    gridWidth = 20,
    gridHeight = 16,
    weightX = 1,
    weightY = 1,
    fil = GridBagConstraints.BOTH
  )

private def spacer4GBC: GridBagConstraints =
  gbc(
    gridX = 0,
    weightX = 1,
    fil = GridBagConstraints.HORIZONTAL
  )

private def generateButtonGBC: GridBagConstraints =
  gbc(gridX = 0, anc = GridBagConstraints.SOUTHEAST)