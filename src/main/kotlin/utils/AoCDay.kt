package utils

abstract class AoCDay(private val day: String) {
    val input = readFile("Day$day")

    fun execute() {
        println("Running Day $day")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

    abstract fun part1(): Any
    abstract fun part2(): Any
}
