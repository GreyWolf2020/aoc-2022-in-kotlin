fun main() {

    fun part1(signal: String, n: Int = 4): Int = firstNUniqueCharacters(signal = signal, n = n)
    fun part2(signal: String, n: Int = 14): Int = firstNUniqueCharacters(signal = signal, n = n)
    // test if implementation meets criteria from the description, like:
    // table test of part1
    val testInput01 = readInput("Day06_test_part01")
    val testInput01Solutions = listOf<Int>(7, 5, 6, 10, 11)
    testInput01
        .zip(testInput01Solutions)
        .map {
            check(part1(it.first) == it.second)
        }

    // table test of part2
    val testInput02 = readInput("Day06_test_part02")
    val testInput02Solutions = listOf<Int>(19, 23, 23, 29, 26)
    testInput02
        .zip(testInput02Solutions)
        .map {
            check(part2(it.first) == it.second)
        }

    val input = readInputAsString("Day06")
    println(part1(input))
    println(part2(input))
}

fun firstNUniqueCharacters(signal: String, n: Int): Int {
    val possiblePacketMarkers = signal.windowed(n)
        .map { it.toSet() }
    var lastPositionOfMarker = n
    for (possiblePacketMarker in possiblePacketMarkers) {
        if (possiblePacketMarker.size == n) {
            break
        }
        lastPositionOfMarker++
    }
    return lastPositionOfMarker
}
