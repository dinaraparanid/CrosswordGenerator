import presentation.MainFrame
import data.generation.population.TableState.showTable
import cats.implicits.*
import domain.generation.generation

@main
def main(): Unit =
  val words = List(
    "akin",
    "reduce",
    "user",
    "collar",
    "city",
    "issued",
    "easily",
    "exam",
    "travel",
    "arch",
    "pencil",
    "tabs",
    "keeps",
    "omits",
    "neutral",
  )

  println(generation(words, 20).show)