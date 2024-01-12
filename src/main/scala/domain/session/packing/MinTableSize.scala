package domain.session.packing

import data.generation.population.Table
import domain.generation.population.EmptyLetter

import zio.{UIO, ZIO}

private def minTableSizePar(table: Table): UIO[(Int, Int, Int, Int)] =
  for {
    top    ← ZIO.succeedBlocking(topBorder(table)).fork
    bottom ← ZIO.succeedBlocking(bottomBorder(table)).fork
    left   ← ZIO.succeedBlocking(leftBorder(table)).fork
    right  ← ZIO.succeedBlocking(rightBorder(table)).fork
    res    ← (top <*> bottom <*> left <*> right).join
  } yield res

private def topBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i1, i2) },
    border = { case (row, _) ⇒ row }
  )

private def bottomBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices.reverse,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i1, i2) },
    border = { case (row, _) ⇒ row }
  )

private def leftBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i2, i1) },
    border = { case (_, column) ⇒ column }
  )

private def rightBorder(table: Table): Int =
  scanBorders(
    table = table,
    firstRange = table.indices.reverse,
    secondRange = table.indices,
    indices = { case (i1, i2) ⇒ (i2, i1) },
    border = { case (_, column) ⇒ column }
  )

@inline
private def scanBorders(
  table:       Table,
  firstRange:  Range,
  secondRange: Range,
  indices:     (Int, Int) ⇒ (Int, Int),
  border:      (Int, Int) ⇒ Int
): Int =
  val res = for {
    i1 ← firstRange
    i2 ← secondRange

    (row, column) = indices(i1, i2)
    value = table(row)(column)
    res = border(row, column) if value != EmptyLetter
  } yield res

  res.head
