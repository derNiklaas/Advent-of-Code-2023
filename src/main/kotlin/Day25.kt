import utils.AoCDay

class Day25 : AoCDay() {
    private val wires = buildMap<String, MutableSet<String>> {
        input.forEach { line ->
            val (name, components) = line.split(": ")
            components.splitToSequence(" ").forEach { component ->
                getOrPut(name) { mutableSetOf() } += component
                getOrPut(component) { mutableSetOf() } += name
            }
        }
    }

    override fun part1(): Any {
        return findThreeBridges()
    }

    override fun part2(): Any {
        return "Merry Christmas!"
    }

    private fun findThreeBridges(): Int {
        val sameComponent = mutableSetOf<Pair<String, String>>()

        wires.keys.forEachIndexed { index, startWire ->
            wires.keys.asSequence().take(index).forEachIndexed { j, endWire ->
                val currentWires = wires.mapValues { it.value.toMutableSet() }.toMutableMap()

                repeat(3) {
                    val visitedEdges = mutableListOf<String>()
                    findPath(currentWires, startWire, endWire, visitedEdges)

                    visitedEdges.asSequence().zipWithNext().forEach { (f, t) ->
                        currentWires[f]!! -= t
                        currentWires[t]!! -= f
                    }
                }
                if (findPath(currentWires, startWire, endWire)) sameComponent += startWire to endWire
            }
        }
        val components = buildMap<String, MutableList<String>> {
            sameComponent.forEach { (f, t) ->
                getOrPut(f) { mutableListOf() } += t
                getOrPut(t) { mutableListOf() } += f
            }
        }

        val visited = mutableSetOf<String>()

        return components.keys.asSequence()
            .map {
                val previousSize = visited.size
                traverse(components, it, visited)
                visited.size - previousSize
            }.filter { it != 0 }
            .toList()
            .let { (a, b) -> a * b }
    }

    private fun findPath(
        wires: Map<String, Collection<String>>,
        currentWire: String,
        endWire: String,
        visitedEdges: MutableList<String> = mutableListOf(),
        visitedVertices: MutableSet<String> = mutableSetOf()
    ): Boolean {
        if (currentWire in visitedVertices) return false
        visitedVertices += currentWire
        visitedEdges += currentWire

        if (currentWire == endWire) return true

        wires[currentWire]?.forEach {
            if (findPath(wires, it, endWire, visitedEdges, visitedVertices)) return true
        }

        visitedEdges.removeLast()
        return false
    }

    private fun traverse(wires: Map<String, Collection<String>>, currentWire: String, visited: MutableSet<String>) {
        if (currentWire in visited) return
        visited += currentWire
        wires[currentWire]?.forEach { traverse(wires, it, visited) }
    }

}

fun main() {
    Day25().execute()
}
