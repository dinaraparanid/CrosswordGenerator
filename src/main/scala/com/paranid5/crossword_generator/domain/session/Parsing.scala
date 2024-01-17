package com.paranid5.crossword_generator.domain.session

private val WordsMeaningRegex = "(.*) - (.*)".r

/**
 * Parses the given [[wordsInput]] into a map of words to their meanings
 * (e.g. "Mars - 4-rth planet ..." => Map(Mars, 4-rth planet))
 * with the list of words themselves
 * 
 * @param wordsInput input to parse
 * @return list of words in lower case,
 *         map of words (lower case)
 *         and their meanings (original case)
 */

def parsedWordsWithMeanings(wordsInput: String): (List[String], Map[String, String]) =
  val pairs = WordsMeaningRegex
    .findAllMatchIn(wordsInput)
    .map { mch ⇒ (mch group 1, mch group 2) }
    .map { case (w, m) ⇒ (w.toLowerCase, m) }
    .toList

  (pairs map (_._1), pairs.toMap)