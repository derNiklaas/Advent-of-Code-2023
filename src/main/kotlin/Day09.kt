import utils.AoCDay
import utils.splitAndMapToInt

class Day09 : AoCDay("09") {
    private val numbers = input.splitAndMapToInt()
    override fun part1(): Any {
        val allSteps = generateSteps()

        allSteps.forEach { steps ->
            steps.last().add(0)
            for (i in steps.size - 2 downTo 0) {
                val index = steps[i].size - 1
                steps[i].add(steps[i][index] + steps[i + 1][index])
            }
        }

        return allSteps.sumOf { it.first().last() }
    }

    override fun part2(): Any {
        val allSteps = generateSteps()
        allSteps.forEach { steps ->
            steps.last().add(0, 0)
            for (i in steps.size - 2 downTo 0) {
                steps[i].add(0, steps[i][0] - steps[i + 1][0])
            }
        }
        return allSteps.sumOf { it.first().first() }
    }

    private fun generateSteps(): MutableList<List<MutableList<Int>>> {
        // index -> Lists
        val allSteps = mutableListOf<List<MutableList<Int>>>()
        numbers.asSequence().forEach { sequence ->
            val steps = mutableListOf<MutableList<Int>>()
            var step = mutableListOf<Int>()
            steps += sequence.toMutableList()
            step += sequence
            while (!step.all { it == 0 }) {
                val nextStep = step.windowed(2).map { (a, b) ->
                    b - a
                }
                steps += nextStep.toMutableList()
                step = nextStep.toMutableList()
            }
            allSteps += steps
        }

        return allSteps
    }
}

fun main() {
    Day09().execute()
}
