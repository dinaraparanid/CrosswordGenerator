package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment

/**
 * Bold, centrally aligned paragraph
 *
 * @param text paragraph text
 * @param fontSize required font size
 * @return leading header
 */

def HeaderParagraph(text: String, fontSize: Int = 24): Paragraph =
  Paragraph(text)
    .setTextAlignment(TextAlignment.CENTER)
    .setFontSize(fontSize)
    .setBold()
