import kotlin.math.abs
import utils.AoCDay
import utils.Vec2D

class Day18 : AoCDay() {
    private val digInstructions = input.map {
        val (dir, length, hexCode) = it.split(" ")
        val direction = when (dir) {
            "U" -> Vec2D.UP
            "D" -> Vec2D.DOWN
            "L" -> Vec2D.LEFT
            "R" -> Vec2D.RIGHT
            else -> error("Illegal Direction $dir")
        }
        val hexNumbers = hexCode.removeSurrounding("(#", ")")
        println(hexNumbers)
        val directionB = when (hexNumbers.last()) {
            '0' -> Vec2D.RIGHT
            '1' -> Vec2D.DOWN
            '2' -> Vec2D.LEFT
            '3' -> Vec2D.UP
            else -> error("Illegal Direction2 ${hexCode.drop(1).last()}")
        }
        val lengthB = hexNumbers.dropLast(1).toInt(16)

        DigInstruction(direction, length.toInt(), directionB, lengthB)
    }

    override fun part1(): Any {
        var pos = Vec2D(0, 0)
        var area = 0L
        var borderLength = 0L
        for (instruction in digInstructions) {
            val newPos = pos + (instruction.direction * instruction.length)
            area += (newPos.y - pos.y).toLong() * (newPos.x + pos.x).toLong()
            borderLength += instruction.length
            pos = newPos
        }
        return abs(area) / 2 + borderLength / 2 + 1


        /*val border = mutableSetOf<Vec2D>()
        var currentPos = Vec2D(0, 0)
        border += currentPos
        digInstructions.forEach {
            for (i in 0..<it.length) {
                currentPos += it.direction
                border += currentPos
            }
        }

        val flooded = flood(border)

        printGrid(border, flooded)

        val minY = border.minBy { it.y }.y
        val maxY = border.maxBy { it.y }.y
        val minX = border.minBy { it.x }.x
        val maxX = border.maxBy { it.x }.x

        return (maxX - minX + 1) * (maxY - minY + 1) - flooded.size */
    }

    override fun part2(): Any {
        var x = 0
        var y = 0
        var area = 0L
        var borderLength = 0L
        for (instruction in digInstructions) {
            val newX = x + (instruction.directionB * instruction.lengthB).x
            val newY = y + (instruction.directionB * instruction.lengthB).y
            area += (newY - y).toLong() * (newX + x).toLong()
            borderLength += instruction.lengthB
            x = newX
            y = newY
        }
        return abs(area) / 2 + borderLength / 2 + 1
    }

    private fun printGrid(border: Set<Vec2D>, flooded: Set<Vec2D>) {
        val minY = border.minBy { it.y }.y
        val maxY = border.maxBy { it.y }.y
        val minX = border.minBy { it.x }.x
        val maxX = border.maxBy { it.x }.x

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (Vec2D(x, y) in border) print("#")
                else if (Vec2D(x, y) in flooded) print('F')
                else print(".")
            }
            println()
        }
    }

    private fun flood(border: Set<Vec2D>): Set<Vec2D> {
        val flooded = mutableSetOf<Vec2D>()
        val minY = border.minBy { it.y }.y
        val maxY = border.maxBy { it.y }.y
        val minX = border.minBy { it.x }.x
        val maxX = border.maxBy { it.x }.x

        val floodQueue = mutableSetOf<Vec2D>()

        for (y in minY..maxY) {
            val left = Vec2D(minX, y)
            val right = Vec2D(maxX, y)
            if (left !in border) floodQueue += left
            if (right !in border) floodQueue += right
        }
        for (x in minX..maxX) {
            val top = Vec2D(x, minY)
            val bottom = Vec2D(x, maxY)
            if (top !in border) floodQueue += top
            if (bottom !in border) floodQueue += bottom
        }

        while (floodQueue.isNotEmpty()) {
            val pos = floodQueue.first()
            floodQueue.remove(pos)

            if (pos in flooded) continue
            if (pos in border) continue
            flooded += pos
            pos.getNeighbours().forEach {
                if (it.x in minX..maxX && it.y in minY..maxY) {
                    floodQueue += it
                }
            }
        }
        return flooded
    }
}

private data class DigInstruction(val direction: Vec2D, val length: Int, val directionB: Vec2D, val lengthB: Int)

fun main() {
    Day18().execute()
}