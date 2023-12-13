import utils.AoCDay

class Day13 : AoCDay() {

    private val maps = input.joinToString("\n").split("\n\n")
        .map { it.split("\n").map { inner -> inner.toCharArray() }.toTypedArray() }

    override fun part1(): Any {
        // 41566
        var sum = 0L
        maps.forEach { map ->
            val reflections = findReflection(map)
            for (reflection in reflections) {
                sum += if (reflection > 0) {
                    reflection
                } else {
                    100 * -reflection
                }
            }
        }
        return sum
    }

    override fun part2(): Any {
        var sum = 0L
        maps.forEach { map ->
            val width = map.size
            val height = map[0].size
            val startReflections = findReflection(map)
            search@ for (i in 0..<width) for (j in 0..<height) {
                val character = map[i][j]
                map[i][j] = when (character) {
                    '.' -> '#'
                    else -> '.'
                }
                val remainingReflections = findReflection(map) - startReflections
                if (remainingReflections.isNotEmpty()) {
                    for (reflections in remainingReflections) {
                        sum += if (reflections > 0) {
                            reflections
                        } else {
                            100 * -reflections
                        }
                    }
                    break@search
                }
                map[i][j] = character
            }
        }
        return sum
    }

    private fun findReflection(map: Array<CharArray>): Set<Int> {
        val reflections = HashSet<Int>()
        val height = map.size
        val width = map[0].size
        for (i in 1..<width) {
            var valid = true
            for (j in 0..<height) {
                for (k in 0..<minOf(width - i, i)) {
                    if (map[j][i - k - 1] != map[j][k + i]) valid = false
                }
            }
            if (valid) reflections += i
        }
        for (i in 1..<height) {
            var valid = true
            for (j in 0..<minOf(height - i, i)) {
                for (k in 0..<width) {
                    if (map[i - j - 1][k] != map[j + i][k]) valid = false
                }
            }
            if (valid) reflections += -i
        }
        return reflections
    }
}

fun main() {
    Day13().execute()
}