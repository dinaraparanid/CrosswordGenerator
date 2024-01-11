package domain.session

import data.app.SessionStates
import presentation.wordsInput
import zio.URIO

private val WordsMeaningRegex = "(.*) - (.*)".r

def parsedWordsWithMeanings(wordsInput: String): Map[String, String] =
  WordsMeaningRegex
    .findAllMatchIn(wordsInput)
    .map { mch ⇒ (mch group 1, mch group 2) }
    .map { case (w, m) ⇒ (w.toLowerCase, m.toLowerCase) }
    .toMap
