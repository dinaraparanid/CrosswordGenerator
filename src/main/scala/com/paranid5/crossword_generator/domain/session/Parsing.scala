package com.paranid5.crossword_generator.domain.session

private val WordsMeaningRegex = "(.*) - (.*)".r

def parsedWordsWithMeanings(wordsInput: String): (List[String], Map[String, String]) =
  val pairs = WordsMeaningRegex
    .findAllMatchIn(wordsInput)
    .map { mch ⇒ (mch group 1, mch group 2) }
    .map { case (w, m) ⇒ (w.toLowerCase, m) }
    .toList

  (pairs map (_._1), pairs.toMap)