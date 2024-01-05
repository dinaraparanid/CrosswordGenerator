import presentation.MainFrame
import data.generation.population.TableState.showTable
import domain.generation.population.initialPopulation
import cats.implicits.*
import domain.generation.generation

@main
def main(): Unit =
	val words = List(
		"cemetery",
		"widow",
		"chemistry",
		"engine",
		"gate",
		"wine",
		"fairytale",
		"pillow",
		"train"
	)

	println(generation(words, 20).show)