import java.util.Stack
import utils.AoCDay
import utils.Vec2D
import utils.to2DCharArray

class Day23 : AoCDay() {
    private val grid = input.to2DCharArray()
    private val start = Vec2D(input.first().indexOf('.'), 0)
    private val end = Vec2D(input.last().indexOf('.'), input.size - 1)
    private var poi = mutableListOf(start, end)

    private var seenPositions = mutableSetOf<Vec2D>()
    private var graph: Map<Vec2D, Map<Vec2D, Int>> = mutableMapOf()

    init {
        generatePOIs()
        buildGraph()
    }

    override fun part1(): Any {
        return dfs(start)
    }

    override fun part2(): Any {
        seenPositions = mutableSetOf()
        buildGraph(true)
        return dfs(start)
    }

    private fun dfs(position: Vec2D): Int {
        if (position == end) {
            return 0
        }

        var maxDistance = Int.MIN_VALUE
        seenPositions += position
        for (entry in graph[position]!!) {
            val (nextPosition, length) = entry
            if (nextPosition !in seenPositions) {
                maxDistance = maxOf(maxDistance, dfs(nextPosition) + length)
            }
        }
        seenPositions -= position

        return maxDistance
    }

    private fun buildGraph(partB: Boolean = false) {
        val graph = mutableMapOf<Vec2D, MutableMap<Vec2D, Int>>()

        for (position in poi) {
            graph[position] = mutableMapOf()
            val stack = Stack<Pair<Vec2D, Int>>()
            stack.add(position to 0)
            val seen = mutableSetOf(position)
            while (stack.isNotEmpty()) {
                val (pos, length) = stack.pop()
                if (length != 0 && pos in poi) {
                    graph[position]!![pos] = length
                    continue
                }
                for (direction in getDirections(grid[pos.y][pos.x], partB)) {
                    val newPos = pos + direction
                    if (newPos.x in input[0].indices && newPos.y in input.indices && grid[newPos.y][newPos.x] != '#' && newPos !in seen) {
                        stack.add(newPos to length + 1)
                        seen += newPos
                    }
                }
            }
        }
        this.graph = graph
    }

    private fun generatePOIs() {
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char != '#') {
                    val pos = Vec2D(x, y)
                    val count = pos.getDirectNeighbours().count {
                        it.x in input[0].indices && it.y in input.indices && grid[it.y][it.x] != '#'
                    }
                    if (count >= 3) {
                        poi += pos
                    }
                }
            }
        }
    }

    private fun getDirections(char: Char, partB: Boolean = false): List<Vec2D> {
        return if (partB) {
            listOf(Vec2D.UP, Vec2D.DOWN, Vec2D.LEFT, Vec2D.RIGHT)
        } else when (char) {
            '^' -> listOf(Vec2D.UP)
            '>' -> listOf(Vec2D.RIGHT)
            '<' -> listOf(Vec2D.LEFT)
            'v' -> listOf(Vec2D.DOWN)
            '.' -> listOf(Vec2D.UP, Vec2D.DOWN, Vec2D.LEFT, Vec2D.RIGHT)
            else -> error("unknown direction $char")
        }
    }

}

fun main() {
    Day23().execute()
}
