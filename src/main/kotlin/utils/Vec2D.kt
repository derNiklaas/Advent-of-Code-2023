package utils

import kotlin.math.abs

data class Vec2D(val x: Int, val y: Int) {

    /**
     * Checks if the [other] Vec2D is a direct neighbour of this Vec2D.
     */
    fun isNeighbour(other: Vec2D) = abs(other.x - x) <= 1 && abs(other.y - y) <= 1

    /**
     * Calculates the manhattan distance between this Vec2D and the [other] Vec2D.
     */
    fun manhattanDistance(other: Vec2D) = abs(other.x - x) + abs(other.y - y)

    /**
     * Returns a list of all direct neighbours of this Vec2D.
     */
    fun getNeighbours() = buildList {
        for (xChange in -1..1) {
            for (yChange in -1..1) {
                if (xChange == 0 && yChange == 0) continue
                add(Vec2D(x + xChange, y + yChange))
            }
        }
    }


    operator fun plus(other: Vec2D) = Vec2D(x + other.x, y + other.y)

    operator fun minus(other: Vec2D) = Vec2D(x - other.x, y - other.y)

    operator fun times(times: Int) = Vec2D(x * times, y * times)
}

operator fun <T> List<List<T>>.get(point: Vec2D): T = this[point.x][point.y]

operator fun <T> List<MutableList<T>>.set(point: Vec2D, t: T) {
    this[point.x][point.y] = t
}

fun <T> List<List<T>>.getOrNull(point: Vec2D): T? = this.getOrNull(point.x)?.getOrNull(point.y)
