import utils.AoCDay
import utils.Vec3D
import utils.splitAndMapToInt

class Day22 : AoCDay() {

    private var bricks = input.map {
        val (start, end) = it.split("~")
        val (sx, sy, sz) = start.splitAndMapToInt(",")
        val (ex, ey, ez) = end.splitAndMapToInt(",")
        Brick(Vec3D(sx, sy, sz), Vec3D(ex, ey, ez))
    }.sortedBy { it.start.z }
    private val keySupportsValue = bricks.indices.associateWith { mutableSetOf<Int>() }
    private val valueSupportsKey = bricks.indices.associateWith { mutableSetOf<Int>() }

    override fun part1(): Any {
        bricks.forEachIndexed { index, brick ->
            var maxZ = 1
            for (checkIndex in 0..<index) {
                val checkBrick = bricks[checkIndex]
                if (overlaps(brick, checkBrick)) {
                    maxZ = maxOf(maxZ, checkBrick.end.z + 1)
                }
            }
            val start = brick.start
            val end = brick.end
            brick.end = Vec3D(end.x, end.y, end.z - (start.z - maxZ))
            brick.start = Vec3D(start.x, start.y, maxZ)
        }

        bricks = bricks.sortedBy { it.start.z }

        bricks.forEachIndexed { brickIndex, brick ->
            for (checkBrickIndex in 0..<brickIndex) {
                val checkBrick = bricks[checkBrickIndex]
                // brick should always be above the checkBrick
                if (overlaps(checkBrick, brick) && brick.start.z == (checkBrick.end.z + 1)) {
                    keySupportsValue[checkBrickIndex]!!.add(brickIndex)
                    valueSupportsKey[brickIndex]!!.add(checkBrickIndex)
                }
            }
        }

        var total = 0
        for (index in bricks.indices) {
            val supports = keySupportsValue[index]!!
            if (supports.all { valueSupportsKey[it]!!.size >= 2 }) {
                total += 1
            }
        }

        return total
    }

    override fun part2(): Any {
        var total = 0L
        for (i in bricks.indices) {
            val queue = ArrayDeque<Int>()

            keySupportsValue[i]!!.forEach {
                if (valueSupportsKey[it]!!.size == 1) {
                    queue += it
                }
            }
            val falling = mutableSetOf(i)
            falling += queue

            while (queue.isNotEmpty()) {
                val currentBrickIndex = queue.removeFirst()
                for (brickSupportedByCurrentIndex in keySupportsValue[currentBrickIndex]!!) {

                    // Ignore blocks that are already falling
                    if (brickSupportedByCurrentIndex in falling) continue

                    // Check if all bricks that support brickSupportedByCurrentIndex and check if they are falling
                    if (valueSupportsKey[brickSupportedByCurrentIndex]!!.all { it in falling }) {
                        queue += brickSupportedByCurrentIndex
                        falling += brickSupportedByCurrentIndex
                    }
                }
            }
            // Remove 1 from the size as "i" is already falling
            total += falling.size - 1
        }
        return total
    }

    private fun overlaps(a: Brick, b: Brick): Boolean {
        return maxOf(a.start.x, b.start.x) <= minOf(a.end.x, b.end.x) && maxOf(a.start.y, b.start.y) <= minOf(
            a.end.y,
            b.end.y
        )
    }
}

private data class Brick(var start: Vec3D, var end: Vec3D)

fun main() {
    Day22().execute()
}