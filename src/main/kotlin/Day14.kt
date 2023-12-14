import utils.AoCDay
import utils.Vec2D
import utils.to2DCharArray

class Day14 : AoCDay() {
    var map = input.to2DCharArray()

    override fun part1(): Any {
        moveNorth()

        return calculateWeight()
    }

    override fun part2(): Any {
        map = input.to2DCharArray()
        // stone -> index
        val rockToIndex = mutableMapOf<List<Vec2D>, Int>()
        val indexToRock = mutableMapOf<Int, List<Vec2D>>()
        var currentRocks = getAllRocks()
        repeat(1_000_000_000) {
            currentRocks = getAllRocks()
            if (currentRocks in rockToIndex) {
                val index = rockToIndex[currentRocks]!!
                val cycleSize = it - index
                val rocks = indexToRock[index + (1_000_000_000 - index) % cycleSize]!!.sumOf { map.size - it.y }
                return rocks
            } else {
                moveNorth()
                moveWest()
                moveSouth()
                moveEast()
                val newRocks = getAllRocks()
                rockToIndex += currentRocks to it
                indexToRock += it to currentRocks
                currentRocks = newRocks
            }

        }
        printMap()
        return currentRocks.sumOf { map.size - it.y }
    }

    private fun calculateWeight(): Int {
        return map.withIndex().sumOf { row ->
            row.value.count { it == 'O' } * (map.size - row.index)
        }
    }

    private fun getAllRocks(): List<Vec2D> {
        val rocks = mutableListOf<Vec2D>()
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char == 'O') rocks += Vec2D(x, y)
            }
        }
        return rocks
    }

    fun printMap() {
        for (y in map.indices) {
            for (x in map[0].indices) {
                print(map[y][x])
            }
            println()
        }
    }

    private fun moveNorth() {
        for (i in map.indices) {
            for (char in map[i].withIndex()) {
                if (char.value == 'O') {
                    if (i == 0) continue
                    if (map[i - 1][char.index] != '.') continue
                    var y = i - 1
                    while (y != 0 && map[y - 1][char.index] == '.') {
                        y--
                    }
                    map[i][char.index] = '.'
                    map[y][char.index] = 'O'
                }
            }
        }
    }

    // <--
    private fun moveWest() {
        for (row in map) {
            for (char in row.withIndex()) {
                if (char.value == 'O') {
                    if (char.index == 0) continue
                    if (row[char.index - 1] != '.') continue
                    var x = char.index - 1
                    while (x != 0 && row[x - 1] == '.') {
                        x--
                    }
                    row[char.index] = '.'
                    row[x] = 'O'
                }
            }
        }
    }

    private fun moveSouth() {
        for (i in map.indices.reversed()) {
            for (char in map[i].withIndex()) {
                if (char.value == 'O') {
                    if (i == map.size - 1) continue
                    if (map[i + 1][char.index] != '.') continue
                    var y = i + 1
                    while (y != map.size - 1 && map[y + 1][char.index] == '.') {
                        y++
                    }
                    map[i][char.index] = '.'
                    map[y][char.index] = 'O'
                }
            }
        }
    }

    private fun moveEast() {
        for (row in map) {
            for (char in row.withIndex().reversed()) {
                if (char.value == 'O') {
                    if (char.index == row.size - 1) continue
                    if (row[char.index + 1] != '.') continue
                    var x = char.index + 1
                    while (x != row.size - 1 && row[x + 1] == '.') {
                        x++
                    }
                    row[char.index] = '.'
                    row[x] = 'O'
                }
            }
        }
    }
}

fun main() {
    Day14().execute()
}