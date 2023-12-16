import utils.AoCDay
import utils.Vec2D

class Day16 : AoCDay() {
    override fun part1(): Any {
        return lightUp(Light(Vec2D(-1, 0), Vec2D.RIGHT))
    }

    override fun part2(): Any {
        return buildList {
            for (x in input[0].indices) {
                add(Light(Vec2D(x, -1), Vec2D.DOWN))
                add(Light(Vec2D(x, input.size), Vec2D.UP))
            }
            for (y in input.indices) {
                add(Light(Vec2D(-1, y), Vec2D.RIGHT))
                add(Light(Vec2D(input[0].length, y), Vec2D.LEFT))
            }
        }.maxOf { lightUp(it) }
    }

    private fun lightUp(start: Light): Int {
        val seen = mutableSetOf<Vec2D>()
        val seenLight = mutableListOf<Light>()
        val queue = mutableListOf<Light>()
        queue += start
        while (queue.isNotEmpty()) {
            val light = queue.removeFirst()
            val (position, direction) = light

            if (light in seenLight) continue

            seen += position
            seenLight += light

            val newPos = position + direction
            if (newPos.y !in input.indices) continue
            if (newPos.x !in input[0].indices) continue
            val char = input[newPos.y][newPos.x]
            when (char) {
                '.' -> queue += Light(newPos, direction)
                '|' -> {
                    if (direction == Vec2D.LEFT || direction == Vec2D.RIGHT) {
                        queue += Light(newPos, Vec2D.UP)
                        queue += Light(newPos, Vec2D.DOWN)
                    } else {
                        queue += Light(newPos, direction)
                    }
                }

                '-' -> {
                    if (direction == Vec2D.UP || direction == Vec2D.DOWN) {
                        queue += Light(newPos, Vec2D.LEFT)
                        queue += Light(newPos, Vec2D.RIGHT)
                    } else {
                        queue += Light(newPos, direction)
                    }
                }

                '/' -> {
                    when (direction) {
                        Vec2D.UP -> queue += Light(newPos, Vec2D.RIGHT)
                        Vec2D.RIGHT -> queue += Light(newPos, Vec2D.UP)
                        Vec2D.DOWN -> queue += Light(newPos, Vec2D.LEFT)
                        Vec2D.LEFT -> queue += Light(newPos, Vec2D.DOWN)
                    }
                }

                '\\' -> {
                    when (direction) {
                        Vec2D.UP -> queue += Light(newPos, Vec2D.LEFT)
                        Vec2D.RIGHT -> queue += Light(newPos, Vec2D.DOWN)
                        Vec2D.DOWN -> queue += Light(newPos, Vec2D.RIGHT)
                        Vec2D.LEFT -> queue += Light(newPos, Vec2D.UP)
                    }
                }
            }
        }

        seen -= start.position
        return seen.size
    }
}

private data class Light(val position: Vec2D, val direction: Vec2D)

fun main() {
    Day16().execute()
}
