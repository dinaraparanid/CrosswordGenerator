package com.paranid5.crossword_generator.domain.session

import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import com.itextpdf.layout.Document

def SessionDocumentWriter(path: String): Document =
  Document(PdfDocument(PdfWriter(path)))