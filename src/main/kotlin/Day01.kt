import utils.AoCDay

class Day01 : AoCDay() {

    val replacementMap = mapOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    val replacementPattern = replacementMap
        .entries
        .map { (word, digit) -> listOf(word, digit.toString()) }
        .flatten()
        .joinToString("|")
        .toRegex()

    override fun part1(): Any {
        return getCalibrationValue(input)
    }

    override fun part2(): Any {
        val fixedInput = input.map { line -> line.indices.mapNotNull { index -> replacementPattern.find(line, index) } }
            .map {
                it.map(MatchResult::value)
                    .map { value -> (value.toIntOrNull() ?: replacementMap[value])?.toString() ?: value }
            }
            .map { it.joinToString("") }

        return getCalibrationValue(fixedInput)
    }

    fun getCalibrationValue(input: List<String>): Int {
        val calibrationValue = input.sumOf { line ->
            val numbers = line.filter { it.isDigit() }
            numbers.first().digitToInt() * 10 + numbers.last().digitToInt()
        }

        return calibrationValue
    }
}

fun main() {
    Day01().execute()
}
