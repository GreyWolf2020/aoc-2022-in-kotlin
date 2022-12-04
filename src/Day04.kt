fun main() {
    fun part1(input: List<String>): Int = input
        .contains(::fullyContains)

    fun part2(input: List<String>): Int = input
        .contains(::partiallyContains)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}


fun List<String>.contains(fullyOrPartialContains: (List<Int>, List<Int>) -> Boolean) = map {
    it
        .split(",")
}
    .foldRight(0) { cleaningAreas, containedAreas ->
        val (cleaningAreaOne, cleaningAreaTwo) = cleaningAreas
            .map { cleaningArea ->
                cleaningArea
                    .split("-")
                    .map { it.toInt() }
            }
        containedAreas + if (fullyOrPartialContains(cleaningAreaOne, cleaningAreaTwo))
            1
        else 0
    }

fun fullyContains(areaOne: List<Int>, areaTwo: List<Int>): Boolean = when {
    areaOne.first() >= areaTwo.first() && areaOne.last() <= areaTwo.last() || areaTwo.first() >= areaOne.first() && areaTwo.last() <= areaOne.last() -> true
    else -> false
}

fun partiallyContains(areaOne: List<Int>, areaTwo: List<Int>): Boolean = (areaOne.first() .. areaOne.last()).toSet().intersect((areaTwo.first() .. areaTwo.last()).toSet()).size >= 1
