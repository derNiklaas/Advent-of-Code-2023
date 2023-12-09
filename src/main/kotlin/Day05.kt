import utils.AoCDay
import utils.splitAndMapToLong

class Day05 : AoCDay("05") {
    private val recipes = mutableListOf<FarmRecipe>()
    private val seeds = input[0].split(": ")[1].splitAndMapToLong()

    init {
        var newMap = true
        var recipe = FarmRecipe("", "", mutableListOf())
        var ranges = mutableListOf<FarmRanges>()
        input.drop(2).forEach { line ->
            if (line.isEmpty()) {
                newMap = true
                recipes += recipe
                return@forEach
            }
            if (newMap) {
                val instruction = line.split(" ")[0].split("-to-")
                newMap = false
                ranges = mutableListOf()
                recipe = FarmRecipe(instruction[0], instruction[1], ranges)
            } else {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = line.splitAndMapToLong()
                ranges += FarmRanges(
                    destinationRangeStart..<destinationRangeStart + rangeLength,
                    sourceRangeStart..<sourceRangeStart + rangeLength
                )
            }
        }
        if (!newMap) recipes += recipe
    }

    override fun part1(): Any {
        val parsedSeeds = seeds.map(::findLocation)
        return parsedSeeds.minOf { it }
    }

    override fun part2(): Any {
        var min = Long.MAX_VALUE
        for (i in seeds.indices step 2) {
            val range = seeds[i]..seeds[i] + seeds[i + 1]
            range.forEach {
                val location = findLocation(it)
                if (location < min) min = location
            }
        }
        return min
    }

    private fun findLocation(seedID: Long): Long {
        return getNextNumber(
            "humidity", getNextNumber(
                "temperature", getNextNumber(
                    "light", getNextNumber(
                        "water", getNextNumber(
                            "fertilizer", getNextNumber(
                                "soil", getNextNumber("seed", seedID)
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getNextNumber(category: String, number: Long): Long {
        val recipe = recipes.find { it.sourceCategory == category } ?: return number
        val ranges = recipe.ranges.find { it.sourceRange.contains(number) } ?: return number
        val offset = number - ranges.sourceRange.first
        return ranges.destinationRange.first + offset
    }
}

private data class FarmRecipe(val sourceCategory: String, val destinationCategory: String, val ranges: List<FarmRanges>)
private data class FarmRanges(val destinationRange: LongRange, val sourceRange: LongRange)

fun main() {
    Day05().execute()
}
