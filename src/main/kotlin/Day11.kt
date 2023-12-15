import utils.AoCDay
import utils.Vec2D
import utils.mapInner

class Day11 : AoCDay() {
    private val galaxies = input.mapInner { it == '#' }

    override fun part1(): Any {
        return solve(1)
    }

    override fun part2(): Any {
        return solve(999_999)
    }

    private fun solve(shift: Int): Long {
        val stations = expandUniverse2ElectricBoogaloo(getStations(galaxies), shift)
        var sum = 0L
        for (i in stations.keys) {
            for (j in i + 1..stations.size) {
                sum += stations[i]!!.manhattanDistance(stations[j]!!)
            }
        }
        return sum
    }

    private fun expandUniverse2ElectricBoogaloo(stations: Map<Int, Vec2D>, shift: Int): Map<Int, Vec2D> {
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
        return newStations
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
