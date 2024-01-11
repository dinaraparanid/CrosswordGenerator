package presentation.menu

import javax.swing.{JMenu, JMenuItem}

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