package utils

data class Vec3D(val x: Int, val y: Int, val z: Int) {

    operator fun plus(other: Vec3D) = Vec3D(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3D) = Vec3D(x - other.x, y - other.y, z - other.z)

    operator fun times(times: Int) = Vec3D(x * times, y * times, z * times)
}

operator fun <T> List<List<List<T>>>.get(point: Vec3D): T = this[point.x][point.y][point.z]

operator fun <T> List<MutableList<MutableList<T>>>.set(point: Vec3D, t: T) {
    this[point.x][point.y][point.z] = t
}

fun <T> List<List<List<T>>>.getOrNull(point: Vec3D): T? =
    this.getOrNull(point.x)?.getOrNull(point.y)?.getOrNull(point.z)
