package utils

import java.io.File

fun readFile(name: String, test: Boolean = false) =
    File("src/${if (test) "test" else "main"}/resources/$name.txt").readLines()

fun List<String>.trimEach() = map(String::trim)

fun List<String>.mapToInt() = map(String::toInt)

fun List<String>.splitAndMapToInt(deliminator: String = " ") = map { it.splitAndMapToInt(deliminator) }

fun List<String>.splitAndMapToInt(deliminator: Regex) = map { it.splitAndMapToInt(deliminator) }

fun String.splitAndMapToInt(deliminator: String = " ") = split(deliminator).filter(String::isNotEmpty).mapToInt()

fun String.splitAndMapToInt(deliminator: Regex) = split(deliminator).filter(String::isNotEmpty).mapToInt()

fun List<String>.mapToLong() = map(String::toLong)

fun List<String>.splitAndMapToLong(deliminator: String = " ") = map { it.splitAndMapToLong(deliminator) }

fun List<String>.splitAndMapToLong(deliminator: Regex) = map { it.splitAndMapToLong(deliminator) }

fun String.splitAndMapToLong(deliminator: String = " ") = split(deliminator).filter(String::isNotEmpty).mapToLong()

fun String.splitAndMapToLong(deliminator: Regex) = split(deliminator).filter(String::isNotEmpty).mapToLong()

fun String.isLowerCase() = all(Char::isLowerCase)

fun String.isUpperCase() = all(Char::isUpperCase)

fun StringBuilder.deletePrefix(length: Int): StringBuilder = delete(0, length)

fun StringBuilder.takeAndDelete(length: Int): String {
    val result = take(length)
    deletePrefix(length)
    return result.toString()
}

fun Set<Int>.allPermutations(): Set<List<Int>> {
    if (this.isEmpty()) return emptySet()
    return _allPermutations(this.toList())
}

// from Tenfour04 on https://stackoverflow.com/a/59737650/9473036

private fun <T> _allPermutations(list: List<T>): Set<List<T>> {
    if (list.isEmpty()) return setOf(emptyList())

    val result: MutableSet<List<T>> = mutableSetOf()
    for (i in list.indices) {
        _allPermutations(list - list[i]).forEach { item -> result.add(item + list[i]) }
    }
    return result
}

fun List<String>.to2DCharArray(): Array<CharArray> {
    return this.map { it.toCharArray() }.toTypedArray()
}

@JvmName("mapInnerCollection")
fun <I, O> Collection<Collection<I>>.mapInner(transform: (I) -> O) = this.map { it.map(transform) }

@JvmName("mapInnerString")
fun <O> Collection<String>.mapInner(transform: (Char) -> O) = this.map { it.toCharArray().map(transform) }

operator fun <T> List<T>.get(range: IntRange) = subList(range.first, range.last + 1)

/** Maps a given int to [0..[other]]*/
infix fun Int.within(other: Int): Int {
    var index = this
    if (this < 0) {
        index += other * (-index / other + 1)
    }
    return index % other
}

@JvmName("lcmWolframInt")
fun Collection<Int>.lcmWolfram(): String {
    return "https://www.wolframalpha.com/input?i2d=true&i=lcm%5C%2840%29${joinToString("%5C%2844%29")}%5C%2841%29"
}

@JvmName("lcmWolframLong")
fun Collection<Long>.lcmWolfram(): String {
    return "https://www.wolframalpha.com/input?i2d=true&i=lcm%5C%2840%29${joinToString("%5C%2844%29")}%5C%2841%29"
}
