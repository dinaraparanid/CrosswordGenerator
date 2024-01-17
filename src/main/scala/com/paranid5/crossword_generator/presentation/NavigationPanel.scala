package com.paranid5.crossword_generator.presentation

import com.paranid5.crossword_generator.data.app.SessionChannel
import com.paranid5.crossword_generator.data.app.navigation.Navigator
import com.paranid5.crossword_generator.data.storage.StoragePreferences
import com.paranid5.crossword_generator.presentation.generation.GenerationScreen

import zio.{RIO, ZIO}

import java.awt.CardLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

/**
 * Composes application navigation panel
 * with all screens to navigate to
 *
 * @return [[RIO]] with both [[CardLayout]] (navigator)
 *         and [[JPanel]] (screen holder)
 *         that completes when content is set
 */

def NavigationPanel(): RIO[StoragePreferences & SessionChannel, (CardLayout, JPanel)] =
  val card = CardLayout()

  val panel = new JPanel(card):
    setBorder(EmptyBorder(0, 10, 5, 10))

  @inline
  def impl(generationScreen: JPanel): Unit =
    panel.add(generationScreen, Navigator.GenerateScreenNav)

  for
    generationScreen ← GenerationScreen()
    _                ← ZIO attempt impl(generationScreen)
  yield (card, panel)
