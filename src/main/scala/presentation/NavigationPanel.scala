package presentation

import java.awt.CardLayout
import javax.swing.JPanel

def NavigationPanel(): (JPanel, CardLayout) =
  val card = CardLayout()
  (JPanel(card), card)
