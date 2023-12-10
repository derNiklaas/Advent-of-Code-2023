package utils

abstract class AoCDay {
    val input = readFile(javaClass.simpleName)

    fun execute() {
        println("Running Day ${javaClass.simpleName.drop(3)}")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

    abstract fun part1(): Any
    abstract fun part2(): Any
}
