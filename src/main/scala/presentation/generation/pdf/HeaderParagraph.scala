package presentation.generation.pdf

import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment

def HeaderParagraph(text: String, fontSize: Int = 24): Paragraph =
  Paragraph(text)
    .setTextAlignment(TextAlignment.CENTER)
    .setFontSize(fontSize)
    .setBold()
