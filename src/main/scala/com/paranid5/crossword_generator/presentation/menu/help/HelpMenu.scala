package com.paranid5.crossword_generator.presentation.menu.help

import javax.swing.{JMenu, JMenuItem}

/**
 * Help menu to receive additional info
 * about application and useful guides
 *
 * @return the whole menu with all items
 */

def HelpMenu(): JMenu =
  new JMenu("Help"):
    add(GuideMenuItem())
    add(AboutAppMenuItem())

private def GuideMenuItem(): JMenuItem =
  new JMenuItem("Guide"):
    addActionListener { _ ⇒ println("TODO: Guide") }

private def AboutAppMenuItem(): JMenuItem =
  new JMenuItem("About App"):
    addActionListener { _ ⇒ println("TODO: About App") }