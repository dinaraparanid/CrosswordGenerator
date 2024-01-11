package presentation.ui.utils

import java.awt.Component
import javax.swing.Box

def VerticalSpacer(height: Int): Component =
  Box.createVerticalStrut(height)

def HorizontalSpacer(width: Int): Component =
  Box.createHorizontalStrut(width)