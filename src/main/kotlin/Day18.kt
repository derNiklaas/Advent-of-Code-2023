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
        return calculateArea()
    }

    override fun part2(): Any {
        return calculateArea(true)
    }

    private fun calculateArea(partB: Boolean = false): Long {
        var pos = Vec2D(0, 0)
        var area = 0L
        var borderLength = 0L
        for (instruction in digInstructions) {
            val newPos =
                if (partB) pos + (instruction.directionB * instruction.lengthB) else pos + (instruction.direction * instruction.length)
            area += (newPos.y - pos.y).toLong() * (newPos.x + pos.x).toLong()
            borderLength += if (partB) instruction.lengthB else instruction.length
            pos = newPos
        }
        return abs(area) / 2 + borderLength / 2 + 1
    }
}

private data class DigInstruction(val direction: Vec2D, val length: Int, val directionB: Vec2D, val lengthB: Int)

fun main() {
    Day18().execute()
}