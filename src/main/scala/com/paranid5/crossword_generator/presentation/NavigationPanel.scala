package com.paranid5.crossword_generator.presentation

import com.paranid5.crossword_generator.data.app.navigation.{NavigationService, Navigator}
import com.paranid5.crossword_generator.data.app.{AppBroadcast, SessionBroadcast}
import com.paranid5.crossword_generator.data.storage.StoragePreferences
import com.paranid5.crossword_generator.presentation.generation.GenerationScreen

import zio.{RIO, Scope, ZIO}

import java.awt.CardLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

def NavigationPanel(): RIO[StoragePreferences & SessionBroadcast & Scope, (CardLayout, JPanel)] =
  val card = CardLayout()

  val panel = new JPanel(card):
    setBorder(EmptyBorder(0, 10, 5, 10))

  def setContentOfPanel(generationScreen: JPanel): Unit =
    panel.add(generationScreen, Navigator.GenerateScreenNav)

  for
    generationScreen ← GenerationScreen()
    _                ← ZIO attempt
      setContentOfPanel(generationScreen)
  yield (card, panel)
