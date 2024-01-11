package domain.session

import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import com.itextpdf.layout.Document

def SessionDocument(path: String): Document =
  Document(PdfDocument(PdfWriter(path)))

def sessionDocumentPath: String = "session.pdf"