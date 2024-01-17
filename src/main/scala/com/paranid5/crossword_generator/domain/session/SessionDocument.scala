package com.paranid5.crossword_generator.domain.session

import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import com.itextpdf.layout.Document

/**
 * Output stream for the session PDF document.
 * Note that document writer has to be closed
 * in order to actually write the content
 * to the requested pdf file
 *
 * @param path path to the pdf doc
 * @return document writer
 */

def SessionDocumentWriter(path: String): Document =
  Document(PdfDocument(PdfWriter(path)))
