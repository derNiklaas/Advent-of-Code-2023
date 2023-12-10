import utils.AoCDay
import utils.Vec2D

class Day03 : AoCDay() {

    private val numbers = mutableMapOf<Vec2D, SerialNumber>()
    private val symbols = mutableMapOf<Vec2D, Char>()

    init {
        var number = 0
        var length = 0
        var lastY = 0
        var id = 0
        input.forEachIndexed { xCord, line ->
            line.forEachIndexed { yCord, char ->
                lastY = yCord
                if (char == '.') {
                    for (y in yCord - 1 downTo yCord - length) {
                        numbers[Vec2D(xCord, y)] = SerialNumber(id, number)
                    }
                    length = 0
                    number = 0
                    id++
                } else if (char.isDigit()) {
                    length++
                    number *= 10
                    number += char.digitToInt()
                } else {
                    if (length != 0) {
                        for (y in yCord - 1 downTo yCord - length) {
                            numbers[Vec2D(xCord, y)] = SerialNumber(id, number)
                        }
                        length = 0
                        number = 0
                        id++
                    }
                    symbols[Vec2D(xCord, yCord)] = char
                }
            }
            for (y in lastY - 1 downTo lastY - length) {
                numbers[Vec2D(xCord, y)] = SerialNumber(id, number)
            }
            length = 0
            number = 0
            id++
        }
    }

    override fun part1(): Any {
        var remaining = numbers.toMutableMap()

        symbols.forEach { (position, _) ->
            position.getNeighbours().forEach { pos ->
                if (numbers[pos] != null) {
                    remaining = remaining.filter { it.value.id != numbers[pos]!!.id }.toMutableMap()
                }
            }
        }
        val validSerialNumbers = numbers.filter { remaining[it.key] == null }.values.distinctBy { it.id }

        return validSerialNumbers.sumOf { it.number }
    }

    override fun part2(): Any {
        val gearSymbols = symbols.filter { it.value == '*' }
        val validGears = mutableListOf<Int>()

        gearSymbols.forEach { (position, _) ->
            val seenIds = mutableSetOf<Int>()
            var gearScore = 1
            val count = position.getNeighbours().count { pos ->
                if (numbers[pos] != null) {
                    val serialNumber = numbers[pos]!!
                    if (serialNumber.id in seenIds) {
                        false
                    } else {
                        seenIds += serialNumber.id
                        gearScore *= serialNumber.number
                        true
                    }
                } else {
                    false
                }
            }

            if (count >= 2) {
                validGears += gearScore
            }
        }

        return validGears.sum()
    }
}

data class SerialNumber(val id: Int, val number: Int)

fun main() {
    Day03().execute()
}
