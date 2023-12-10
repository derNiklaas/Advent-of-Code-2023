import utils.AoCDay
import utils.Vec2D

class Day10 : AoCDay("10") {
    private val pipes = buildMap {
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                val value = when (char) {
                    '|' -> Pipe.VERTICAL
                    '-' -> Pipe.HORIZONTAL
                    'L' -> Pipe.L
                    'J' -> Pipe.J
                    '7' -> Pipe.SEVEN
                    'F' -> Pipe.F
                    'S' -> Pipe.START
                    else -> {
                        Pipe.NOTHING
                    }
                }
                put(Vec2D(x, y), value)
            }
        }
    }
    private val distancesFromStart = mutableMapOf<Vec2D, Int>()

    override fun part1(): Any {

        val queue = mutableListOf<Vec2D>()
        val start = pipes.entries.find { it.value == Pipe.START }!!
        queue += start.key
        distancesFromStart += start.key to 0
        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            val pipe = pipes[head]!!
            val distance = distancesFromStart[head]!! + 1
            pipe.connections.forEach {
                val newConnection = head + it
                if (newConnection !in distancesFromStart.keys && hasConnection(head, newConnection)) {
                    queue += newConnection
                    distancesFromStart[newConnection] = distance
                }
            }
        }
        return distancesFromStart.maxOf { it.value }
    }

    override fun part2(): Any {
        return getEnclosedTiles().size
    }

    private fun hasConnection(pos1: Vec2D, pos2: Vec2D): Boolean {
        val pipe1 = pipes[pos1] ?: return false
        val pipe2 = pipes[pos2] ?: return false

        var twoToOne = false
        var oneToTwo = false

        pipe2.connections.forEach {
            val newPos = pos2 + it
            if (newPos.x == pos1.x && newPos.y == pos1.y) {
                twoToOne = true
            }
        }
        pipe1.connections.forEach {
            val newPos = pos1 + it
            if (newPos.x == pos2.x && newPos.y == pos2.y) {
                oneToTwo = true
            }
        }

        return oneToTwo && twoToOne
    }

    private fun getEnclosedTiles(): List<Vec2D> {
        val enclosedTiles = mutableListOf<Vec2D>()

        for (y in input.indices) {
            var inside = false
            var tubeCount = 0
            var previousPipe = Pipe.NOTHING
            for (x in input[0].indices) {
                val position = Vec2D(x, y)
                var pipe = pipes[position]!!
                if (position in distancesFromStart.keys && pipe != Pipe.HORIZONTAL) {
                    if (pipe == Pipe.START) {
                        pipe = getPipeForStart(position)
                    }
                    tubeCount++
                    if ((pipe == Pipe.J && previousPipe == Pipe.F) || (pipe == Pipe.SEVEN && previousPipe == Pipe.L)) {
                        tubeCount--
                    }
                    previousPipe = pipe
                    inside = tubeCount % 2 == 1
                }
                if (inside && position !in distancesFromStart.keys) {
                    enclosedTiles += position
                }
            }
        }

        return enclosedTiles
    }

    private fun getPipeForStart(startPosition: Vec2D): Pipe {
        val northConnection = hasConnection(startPosition, startPosition + Vec2D.UP)
        val eastConnection = hasConnection(startPosition, startPosition + Vec2D.RIGHT)
        val westConnection = hasConnection(startPosition, startPosition + Vec2D.LEFT)
        val southConnection = hasConnection(startPosition, startPosition + Vec2D.DOWN)
        return if (northConnection && southConnection) {
            Pipe.VERTICAL
        } else if (westConnection && eastConnection) {
            Pipe.HORIZONTAL
        } else if (northConnection && eastConnection) {
            Pipe.L
        } else if (northConnection && westConnection) {
            Pipe.J
        } else if (southConnection && westConnection) {
            Pipe.SEVEN
        } else if (southConnection && eastConnection) {
            Pipe.F
        } else {
            error("illegal")
        }
    }
}

enum class Pipe(val connections: Set<Vec2D>) {
    VERTICAL(setOf(Vec2D.UP, Vec2D.DOWN)), HORIZONTAL(setOf(Vec2D.LEFT, Vec2D.RIGHT)), L(
        setOf(
            Vec2D.UP, Vec2D.RIGHT
        )
    ),
    J(setOf(Vec2D.UP, Vec2D.LEFT)), SEVEN(setOf(Vec2D.DOWN, Vec2D.LEFT)), F(
        setOf(
            Vec2D.DOWN, Vec2D.RIGHT
        )
    ),
    START(setOf(Vec2D.UP, Vec2D.DOWN, Vec2D.LEFT, Vec2D.RIGHT)), NOTHING(setOf())
}

fun main() {
    Day10().execute()
}