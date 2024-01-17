package com.paranid5.crossword_generator.presentation.ui.utils

import java.awt.Component
import javax.swing.Box

/**
 * Vertical spacer to provide
 * margin/padding between components
 *
 * @param height height of the spacer
 * @return spacer with the given height
 */

def VerticalSpacer(height: Int): Component =
  Box createVerticalStrut height

/**
 * Horizontal spacer to provide
 * margin/padding between components
 *
 * @param width width of the spacer
 * @return spacer with the given width
 */

def HorizontalSpacer(width: Int): Component =
  Box createHorizontalStrut width