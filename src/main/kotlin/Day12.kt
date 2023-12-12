import utils.AoCDay
import utils.splitAndMapToInt

class Day12 : AoCDay() {
    private val springs = input.map { row ->
        val (map, groups) = row.split(" ")
        val status = map.map {
            when (it) {
                '#' -> Spring.DAMAGED
                '.' -> Spring.OPERATIONAL
                '?' -> Spring.UNKNOWN
                else -> {
                    error("unknown spring type $it")
                }
            }
        }
        SpringList(status, groups.splitAndMapToInt(","))
    }

    private val springsB = input.map { row ->
        var (map, groups) = row.split(" ")
        map = "$map?$map?$map?$map?$map"
        groups = "$groups,$groups,$groups,$groups,$groups"
        val status = map.map {
            when (it) {
                '#' -> Spring.DAMAGED
                '.' -> Spring.OPERATIONAL
                '?' -> Spring.UNKNOWN
                else -> {
                    error("unknown spring type $it")
                }
            }
        }
        SpringList(status, groups.splitAndMapToInt(","))
    }

    override fun part1(): Any {
        val sum = springs.sumOf { springs ->
            val cache = Array(springs.springs.size) {
                Array(springs.damagedGroups.size + 1) { j ->
                    LongArray(springs.damagedGroups.getOrElse(j) { 0 } + 1) { -1 }
                }
            }
            count(springs.springs, springs.damagedGroups, cache, 0, 0, 0)
        }
        return sum
    }

    override fun part2(): Any {
        val sum = springsB.sumOf { springs ->
            val cache = Array(springs.springs.size) {
                Array(springs.damagedGroups.size + 1) { j ->
                    LongArray(springs.damagedGroups.getOrElse(j) { 0 } + 1) { -1 }
                }
            }
            count(springs.springs, springs.damagedGroups, cache, 0, 0, 0)
        }
        return sum
    }

    private fun count(
        springs: List<Spring>,
        groups: List<Int>,
        cache: Array<Array<LongArray>>,
        i: Int,
        j: Int,
        u: Int
    ): Long {
        if (i >= springs.size) return if (j >= groups.size || j == groups.size - 1 && u == groups[j]) 1 else 0
        if (cache[i][j][u] >= 0) return cache[i][j][u]
        val c = springs[i]
        var result = 0L
        if (c == Spring.OPERATIONAL || c == Spring.UNKNOWN) {
            if (u > 0 && groups[j] == u) {
                result += count(springs, groups, cache, i + 1, j + 1, 0)
            } else if (u == 0) {
                result += count(springs, groups, cache, i + 1, j, 0)
            }
        }
        if (c == Spring.DAMAGED || c == Spring.UNKNOWN) {
            if (j < groups.size && u < groups[j]) {
                result += count(springs, groups, cache, i + 1, j, u + 1)
            }
        }
        cache[i][j][u] = result
        return result
    }
}

data class SpringList(val springs: List<Spring>, val damagedGroups: List<Int>)
enum class Spring(private val char: String) {
    OPERATIONAL("."), DAMAGED("#"), UNKNOWN("?");

    override fun toString(): String {
        return char
    }
}

fun main() {
    Day12().execute()
}
