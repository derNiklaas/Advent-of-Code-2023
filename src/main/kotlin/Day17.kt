import utils.AoCDay
import utils.Direction
import utils.Vec2D
import utils.graph.findShortestPathByPredicate
import utils.mapInner

class Day17 : AoCDay() {
    private val heights = input.mapInner { it.digitToInt() }
    private val height = heights.size
    private val width = heights[0].size

    override fun part1(): Any {
        val start = HeightData(Vec2D(0, 0), Direction.RIGHT, 0)
        val end = Vec2D(heights[0].lastIndex, heights.lastIndex)

        val path = findShortestPathByPredicate(
            start,
            { (position, _) -> position == end },
            { it.neighbours().filter { (position) -> position.x in 0..<width && position.y in 0..<height } },
            { _, (position) -> heights[position.y][position.x] }
        )
        return path.getScore()
    }

    override fun part2(): Any {
        val map = input.map { line -> line.map { it.digitToInt() } }
        val start = HeightData(Vec2D(0, 0), Direction.RIGHT, 0)
        val end = Vec2D(heights[0].lastIndex, heights.lastIndex)

        val path = findShortestPathByPredicate(
            start,
            { (position, _, length) -> position == end && length >= 4 },
            { it.neighboursB().filter { (position) -> position.x in 0..<width && position.y in 0..<height } },
            { _, (position) -> map[position.y][position.x] }
        )
        return path.getScore()
    }

}

private data class HeightData(val position: Vec2D, val direction: Direction, val length: Int) {
    fun neighbours(): List<HeightData> {
        return buildList {
            if (length < 3) {
                add(HeightData(position + direction.pointPositiveDown, direction, length + 1))
            }
            add(HeightData(position + direction.right.pointPositiveDown, direction.right, 1))
            add(HeightData(position + direction.left.pointPositiveDown, direction.left, 1))
        }
    }

    fun neighboursB(): List<HeightData> {
        return buildList {
            if (length < 10) {
                add(HeightData(position + direction.pointPositiveDown, direction, length + 1))
            }
            if (length >= 4 || length == 0) {
                add(HeightData(position + direction.right.pointPositiveDown, direction.right, 1))
                add(HeightData(position + direction.left.pointPositiveDown, direction.left, 1))
            }
        }
    }
}

fun main() {
    Day17().execute()
}
