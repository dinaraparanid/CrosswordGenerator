package com.paranid5.crossword_generator.presentation

import com.paranid5.crossword_generator.data.FullEnvironment
import com.paranid5.crossword_generator.presentation.menu.MainMenuBar

import zio.{RIO, ZIO}

import java.awt.Frame
import javax.swing.{JFrame, JPanel, JMenuBar, WindowConstants}

/**
 * Composes application's main frame
 *
 * @return [[RIO]] with [[JFrame]]
 *         that completes when all content is set
 */

def MainFrame(): RIO[FullEnvironment, JFrame] =
  val frame = new JFrame("Crossword Generator"):
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    setExtendedState(Frame.MAXIMIZED_BOTH)
    setLocationRelativeTo(null)

  @inline
  def impl(navPanel: JPanel, mainMenu: JMenuBar): Unit =
    frame add navPanel
    frame setJMenuBar mainMenu

  for
    cardPanel     ← NavigationPanel()
    (card, panel) = cardPanel
    
    app ← appConfigBroadcast()
    _   ← app.startThemeMonitoring(frame).fork

    navService ← navigationService()
    _          ← navService.invalidate(panel, card)

    mainMenu ← MainMenuBar()
    _        ← ZIO attempt impl(panel, mainMenu)
  yield frame
