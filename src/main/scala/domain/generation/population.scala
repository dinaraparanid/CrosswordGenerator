package domain.generation

import data.generation.population.*
import domain.generation.population.startCoords

import scala.util.Random

def initialPopulation(words: List[String], random: Random): Unit = {

}


private def wordTable(words: List[String], random: Random): Unit = {

}

private def wordStates(
	words: List[String],
	table: Table,
	horizontalWords: List[WordState],
	verticalWords: List[WordState],
	random: Random
): Unit = {
  
}

private def wordState(
	word: String,
	table: Table,
	horizontalWords: List[WordState],
	verticalWords: List[WordState],
	random: Random
) = LazyList
	.iterate(random nextInt 2) { _ ⇒ random nextInt 2 }
	.map { Layout values _ }
	.map { layout ⇒
		val Coords(row, column, _) = startCoords(word, table.length, layout, random)
		CoordsWithLayout(row, column, layout)
	}