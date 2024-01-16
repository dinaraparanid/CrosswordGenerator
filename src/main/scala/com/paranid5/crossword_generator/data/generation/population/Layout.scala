package com.paranid5.crossword_generator.data.generation.population

import scala.util.Random

/** Word' placement position in the table */

enum Layout:
  case HORIZONTAL, VERTICAL

extension (random: Random)
  /** Generates one of two possible layouts */
  def nextLayout: Layout =
    Layout values (random nextInt 2)
