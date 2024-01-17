package com.paranid5.crossword_generator.domain.utils

extension[T] (list: List[T])
  /**
   * Safe version of [[List.tail]].
   * Provides an empty list, if the list is empty
   *
   * @return the rest of the collection without its first element,
   *         or [[Nil]] if the list was empty
   */

  def tailOrNil: List[T] =
    list.tail match
      case Nil       ⇒ Nil
      case _ :: next ⇒ next
