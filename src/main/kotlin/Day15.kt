import utils.AoCDay

class Day15 : AoCDay() {
    private val groups = input.first().split(",")

    override fun part1(): Any {
        return groups.sumOf { hash(it) }
    }

    override fun part2(): Any {
        val hashes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
        repeat(255) {
            hashes += it to mutableListOf()
        }

        groups.forEach { group ->
            if ("-" in group) {
                val name = group.split("-")[0]
                hashes[hash(name)]?.removeIf {
                    it.first == name
                }
            } else if ("=" in group) {
                val (name, focalLength) = group.split("=")

                val hash = hash(name)
                var found = false
                hashes[hash]!!.replaceAll {
                    if (it.first == name) {
                        found = true
                        it.first to focalLength.toInt()
                    } else {
                        it.first to it.second
                    }
                }
                if (!found) {
                    hashes[hash]!!.add(name to focalLength.toInt())
                }
            }


        }
        var sum = 0L
        hashes.forEach { (boxIndex, box) ->
            box.forEachIndexed { index, data ->
                sum += (boxIndex + 1) * (index + 1) * data.second
            }
        }
        printHashes(hashes)
        return sum
    }

    private fun hash(data: String): Int {
        var currentValue = 0
        for (char in data) {
            currentValue += char.code
            currentValue *= 17
            currentValue %= 256
        }
        return currentValue
    }

    private fun printHashes(hashes: Map<Int, MutableList<Pair<String, Int>>>) {
        hashes.forEach { (key, value) ->
            if (value.isEmpty()) return@forEach

            println("$key | $value")
        }
    }
}

fun main() {
    Day15().execute()
}
