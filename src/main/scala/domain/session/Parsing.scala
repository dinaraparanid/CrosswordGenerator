package domain.session

private val WordsMeaningRegex = "(.*) - (.*)".r

def parsedWordsWithMeanings(wordsInput: String): Map[String, String] =
  WordsMeaningRegex
    .findAllMatchIn(wordsInput)
    .map { mch ⇒ (mch group 1, mch group 2) }
    .map { case (w, m) ⇒ (w.toLowerCase, m) }
    .toMap
