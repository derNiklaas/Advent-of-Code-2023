import utils.AoCDay
import utils.splitAndMapToLong

class Day06 : AoCDay("06") {

    private val races = mutableListOf<Race>()

    override fun part1(): Any {
        parseInput()
        var output = 1L
        races.forEach { race ->
            var count = 0
            for (i in race.time downTo 0) {
                if (race.minDistance < i * (race.time - i)) {
                    count++
                }
            }
            if (count != 0) {
                output *= count
            }
        }
        return output
    }

    override fun part2(): Any {
        parseInput(true)
        val race = races.first()

        var count = 0
        for (i in race.time downTo 0) {
            if (race.minDistance < i * (race.time - i)) {
                count++
            }
        }

        return count
    }

    private fun parseInput(b: Boolean = false) {
        races.clear()
        val times = mutableListOf<Long>()
        val distances = mutableListOf<Long>()
        input.map { line ->
            var numbers = line.split(":")[1]
            if (!b) {
                while (numbers.contains("  ")) {
                    numbers = numbers.replace("  ", " ")
                }
            } else {
                numbers = numbers.replace(" ", "")
            }
            if (times.isEmpty()) {
                times += numbers.splitAndMapToLong()
            } else {
                distances += numbers.splitAndMapToLong()
            }
        }
        times.forEachIndexed { index, _ ->
            races += Race(times[index], distances[index])
        }
    }
}

private data class Race(val time: Long, val minDistance: Long)

fun main() {
    Day06().execute()
}
