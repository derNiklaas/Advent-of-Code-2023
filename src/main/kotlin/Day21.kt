import kotlin.math.pow
import utils.AoCDay
import utils.Vec2D

class Day21 : AoCDay() {
    val map = input.mapIndexed { y, row ->
        row.mapIndexed { x, char ->
            Vec2D(x, y) to char
        }
    }.flatten().toMap()

    override fun part1(): Any {
        val start = map.entries.find { it.value == 'S' }?.key ?: error("No start found")
        return makeMoves(start, 64)
    }

    /*
    This does not run on the test input, but does on the real input due to some observations:
    1. The map is square
    2. The start is always in the middle of this square
    3. The amount of steps required fulfills this equation: steps % size == size / 2
     */
    override fun part2(): Any {
        assert(input.size == input[0].length)
        val size = input.size
        val start = map.entries.find { it.value == 'S' }?.key ?: error("No start found")
        val steps = 26501365
        assert(start == Vec2D(size / 2, size / 2))
        assert(steps % size == size / 2)
        val gridWidth = steps / size - 1
        val odd = ((gridWidth / 2) * 2 + 1).toDouble().pow(2).toLong()
        val even = ((gridWidth + 1) / 2 * 2).toDouble().pow(2).toLong()

        // find all points for ONE grid in an even/odd state. size * 2 should be enough
        val oddPoints = makeMoves(start, size * 2 + 1)
        val evenPoints = makeMoves(start, size * 2)

        // calculate the far edges
        val topCorner = makeMoves(Vec2D(start.x, size - 1), size - 1)
        val rightCorner = makeMoves(Vec2D(0, start.y), size - 1)
        val leftCorner = makeMoves(Vec2D(size - 1, start.y), size - 1)
        val bottomCorner = makeMoves(Vec2D(start.x, 0), size - 1)

        // calculate the small diagonal bits
        val topRightSmall = makeMoves(Vec2D(0, size - 1), size / 2 - 1)
        val topLeftSmall = makeMoves(Vec2D(size - 1, size - 1), size / 2 - 1)
        val bottomRightSmall = makeMoves(Vec2D(0, 0), size / 2 - 1)
        val bottomLeftSmall = makeMoves(Vec2D(size - 1, 0), size / 2 - 1)

        // calculate the "bigger" diagonal bits
        val topRightLarge = makeMoves(Vec2D(0, size - 1), (size * 3) / 2 - 1)
        val topLeftLarge = makeMoves(Vec2D(size - 1, size - 1), (size * 3) / 2 - 1)
        val bottomRightLarge = makeMoves(Vec2D(0, 0), (size * 3) / 2 - 1)
        val bottomLeftLarge = makeMoves(Vec2D(size-1, 0), (size * 3) / 2 - 1)

        val smallResult = (gridWidth + 1).toLong() * (topRightSmall + topLeftSmall + bottomLeftSmall + bottomRightSmall)
        val largeResult = gridWidth.toLong() * (topRightLarge + topLeftLarge + bottomLeftLarge + bottomRightLarge)

        return odd * oddPoints + even * evenPoints + topCorner + leftCorner + bottomCorner + rightCorner + smallResult + largeResult
    }

    private fun makeMoves(startPos: Vec2D, steps: Int): Int {
        val answer = mutableSetOf<Vec2D>()
        val seen = mutableSetOf(startPos)
        val queue = ArrayDeque<Pair<Vec2D, Int>>()
        queue += startPos to steps
        while (queue.isNotEmpty()) {
            val (position, step) = queue.removeFirst()
            if (step % 2 == 0) {
                answer += position
            }
            if (step == 0) continue

            position.getDirectNeighbours().forEach { pos ->
                if (map[pos] != null && map[pos] != '#' && pos !in seen) {
                    seen += pos
                    queue += pos to step - 1
                }
            }
        }
        return answer.toList().size
    }
}

fun main() {
    Day21().execute()
}
