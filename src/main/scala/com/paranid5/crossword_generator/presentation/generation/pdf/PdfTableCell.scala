package com.paranid5.crossword_generator.presentation.generation.pdf

import com.itextpdf.layout.element.{Cell, Paragraph}

def TextCell(text: String): Cell =
  Cell()
    .setPaddingLeft(5F)
    .setPaddingRight(50F)
    .add(Paragraph(text))
