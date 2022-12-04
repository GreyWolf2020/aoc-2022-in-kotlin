fun main() {
    fun part1(input: List<String>): Int = input
            .map {
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
                if (isFullyContained(cleaningAreaOne, cleaningAreaTwo))
                    containedAreas + 1
                else containedAreas
            }

    fun part2(input: List<String>): Int = input
        .map {
            it.split(",")
        }
        .foldRight(0) { cleaningAreas, containedAreas ->
            val (cleaningAreaOne, cleaningAreaTwo) = cleaningAreas
                .map { cleaningArea ->
                    cleaningArea
                        .split("-")
                        .map { it.toInt() }
                }
            containedAreas + if (isPartiallyContained(cleaningAreaOne, cleaningAreaTwo)) 0 else 1
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun isFullyContained(areaOne: List<Int>, areaTwo: List<Int>): Boolean = when {
    areaOne.first() >= areaTwo.first() && areaOne.last() <= areaTwo.last() || areaTwo.first() >= areaOne.first() && areaTwo.last() <= areaOne.last() -> true
    else -> false
}

fun isPartiallyContained(areaOne: List<Int>, areaTwo: List<Int>): Boolean = (areaOne.first() .. areaOne.last()).toSet().intersect((areaTwo.first() .. areaTwo.last()).toSet()).size == 0
