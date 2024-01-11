package presentation.generation

import org.icepdf.ri.common.{SwingController, SwingViewBuilder}

import javax.swing.JSplitPane

def CrosswordSheetView(): JSplitPane =
  val controller = new SwingController:
    openDocument("Java_Swing_2nd_Edition.pdf")

  SwingViewBuilder(controller)
    .buildUtilityAndDocumentSplitPane(true)
