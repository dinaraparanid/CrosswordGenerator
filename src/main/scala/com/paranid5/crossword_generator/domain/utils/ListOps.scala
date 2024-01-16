package com.paranid5.crossword_generator.domain.utils

extension[T] (list: List[T])
  def tailOrNil: List[T] =
    list match
      case Nil       ⇒ Nil
      case _ :: next ⇒ next