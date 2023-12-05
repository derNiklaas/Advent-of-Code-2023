import kotlin.time.measureTime
import utils.AoCDay
import utils.splitAndMapToLong

class Day05 : AoCDay("05") {
    private val recipes = mutableListOf<FarmRecipe>()
    val seeds = input[0].split(": ")[1].splitAndMapToLong()

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
        var soilNumber = -1L
        var fertilizerNumber = -1L
        var waterNumber = -1L
        var lightNumber = -1L
        var temperatureNumber = -1L
        var humidityNumber = -1L
        var locationNumber = -1L
        for (i in 1..7) {
            when (i) {
                1 -> soilNumber = getNextNumber("seed", seedID)
                2 -> fertilizerNumber = getNextNumber("soil", soilNumber)
                3 -> waterNumber = getNextNumber("fertilizer", fertilizerNumber)
                4 -> lightNumber = getNextNumber("water", waterNumber)
                5 -> temperatureNumber = getNextNumber("light", lightNumber)
                6 -> humidityNumber = getNextNumber("temperature", temperatureNumber)
                7 -> locationNumber = getNextNumber("humidity", humidityNumber)
            }
        }
        return locationNumber
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
