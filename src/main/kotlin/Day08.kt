import utils.AoCDay

class Day08 : AoCDay("08") {

    private val instructions = input.first().trim().toCharArray().map { it == 'L' }
    private val nodes = input.drop(2).map { line ->
        val (start, ends) = line.split(" = ")
        val (left, right) = ends.replace("(", "").replace(")", "").split(", ")
        Node(start, left, right)
    }

    override fun part1(): Any {
        return finishOne("AAA")
    }

    override fun part2(): Any {
        val currentNodes = nodes.filter { it.name.endsWith("A") }.map { finishOne(it.name) }
        return "https://www.wolframalpha.com/input?i2d=true&i=lcm%5C%2840%29${currentNodes.joinToString("%5C%2844%29")}%5C%2841%29"
    }

    private fun finishOne(start: String): Long {
        var currentNode = nodes.find { it.name == start }!!
        var currentCounter = 0
        var steps = 0L
        while (!currentNode.name.endsWith("Z")) {
            val instruction = instructions[currentCounter]
            currentNode = if (instruction) {
                nodes.find { it.name == currentNode.left }!!
            } else {
                nodes.find { it.name == currentNode.right }!!
            }
            currentCounter = (currentCounter + 1) % instructions.size
            steps++
        }
        return steps
    }
}

data class Node(val name: String, val left: String, val right: String)

fun main() {
    Day08().execute()
}