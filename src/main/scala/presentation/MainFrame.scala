package presentation

import java.awt.Rectangle
import javax.swing.{JFrame, WindowConstants}

def MainFrame(): JFrame =
  val frame = JFrame("Crossword Generator")
  frame setDefaultCloseOperation WindowConstants.EXIT_ON_CLOSE
  frame setBounds Rectangle(0, 0, 1900, 1000)
  frame
