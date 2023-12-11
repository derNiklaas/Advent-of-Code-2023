import kotlin.math.abs
import utils.AoCDay
import utils.Vec2D

class Day11 : AoCDay() {
    private val galaxies = input.map { row -> row.map { it == '#' } }
    override fun part1(): Any {
        val stations = expandUniverse2ElectricBoogaloo(getStations(galaxies))
        var sum = 0
        for (i in stations.keys) {
            for (j in i + 1..stations.size) {
                sum += getDistanceBetween(stations[i]!!, stations[j]!!)
            }
        }
        return sum
    }

    override fun part2(): Any {
        val stations = expandUniverse2ElectricBoogaloo(getStations(galaxies), 999_999)
        var sum = 0L
        for (i in stations.keys) {
            for (j in i + 1..stations.size) {
                sum += getDistanceBetween(stations[i]!!, stations[j]!!)
            }
        }
        return sum
    }

    private fun expandUniverse2ElectricBoogaloo(stations: Map<Int, Vec2D>, shift: Int = 1): Map<Int, Vec2D> {
        var newStations = stations.toMutableMap()
        var yShift = 0L
        galaxies.forEachIndexed { rowIndex, row ->
            if (row.none { it }) {
                val shiftedStations = mutableMapOf<Int, Vec2D>()
                newStations.forEach {
                    shiftedStations += if (it.value.y > rowIndex + yShift) {
                        it.key to Vec2D(0, shift) + it.value
                    } else {
                        it.key to it.value
                    }
                }
                newStations = shiftedStations
                yShift += shift
            }
        }
        var xShift = 0L
        for (i in galaxies[0].indices) {
            var addCurrentColumn = true
            for (j in input.indices) {
                if (galaxies[j][i]) {
                    addCurrentColumn = false
                }
            }
            if (addCurrentColumn) {
                val shiftedStations = mutableMapOf<Int, Vec2D>()
                newStations.forEach {
                    shiftedStations += if (it.value.x > i + xShift) {
                        it.key to Vec2D(shift, 0) + it.value
                    } else {
                        it.key to it.value
                    }
                }
                newStations = shiftedStations
                xShift += shift
            }
        }
        println("max shift x $xShift y $yShift")
        return newStations
    }

    private fun getDistanceBetween(start: Vec2D, end: Vec2D): Int {
        return abs(start.x - end.x) + abs(start.y - end.y)
    }

    private fun getStations(grid: List<List<Boolean>>): Map<Int, Vec2D> {
        val stations = mutableMapOf<Int, Vec2D>()
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                if (grid[y][x]) {
                    stations += (stations.size + 1) to Vec2D(x, y)
                }
            }
        }
        return stations
    }
}

fun main() {
    Day11().execute()
}
