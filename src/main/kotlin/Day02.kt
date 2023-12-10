import utils.AoCDay
import utils.trimEach

class Day02 : AoCDay() {

    private val games = input.map(Game::fromInput)

    override fun part1(): Any {
        val max = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        return games.sumOf { game ->
            val impossible = game.rounds.none { round ->
                round.red > max["red"]!! || round.green > max["green"]!! || round.blue > max["blue"]!!
            }
            if (impossible) game.id else 0
        }
    }

    override fun part2(): Any {
        return games.sumOf {
            val maxRed = it.rounds.maxOf { round -> round.red }
            val maxGreen = it.rounds.maxOf { round -> round.green }
            val maxBlue = it.rounds.maxOf { round -> round.blue }
            maxRed * maxGreen * maxBlue
        }
    }
}

private data class Round(val red: Int, val green: Int, val blue: Int)
private data class Game(val id: Int, val rounds: List<Round>) {
    companion object {
        fun fromInput(input: String): Game {
            val id = input.split(": ")[0].removePrefix("Game").trim().toInt()
            val rounds = input.split(": ")[1].split(";").trimEach()
            val roundsList = mutableListOf<Round>()
            rounds.forEach { round ->
                var red = 0
                var green = 0
                var blue = 0
                val parts = round.split(",").trimEach()
                parts.forEach { part ->
                    val (value, color) = part.split(" ")
                    when (color) {
                        "red" -> red = value.toInt()
                        "blue" -> blue = value.toInt()
                        "green" -> green = value.toInt()
                        else -> error("Illegal color $color")
                    }
                }
                roundsList += Round(red, green, blue)
            }
            return Game(id, roundsList.toList())
        }
    }
}

fun main() {
    Day02().execute()
}
