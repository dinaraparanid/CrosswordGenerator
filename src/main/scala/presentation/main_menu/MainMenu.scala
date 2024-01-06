package presentation.main_menu

import data.app.AppConfig
import presentation.ui.{Theme, appTheme}
import zio.ZIO

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.{JButton, JPanel}

def MainMenu(): ZIO[AppConfig, Nothing, JPanel] = {
  def impl(generateButton: JButton, theme: Theme): JPanel =
    new JPanel {
      setLayout(GridBagLayout())

      val gbc: GridBagConstraints = GridBagConstraints()
      gbc.gridwidth = GridBagConstraints.REMAINDER

      add(generateButton, gbc)
      setBackground(theme.backgroundColor)
    }

  for {
    theme ← appTheme()
    generateButton ← GenerateButton()
  } yield impl(generateButton, theme)
}
