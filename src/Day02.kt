
/*enum class RockPaperScissorsPermutation(val result: Int) {
    `A X`(3 + 1), `A Y`(6 + 2), `A Z`(0 + 3),
    `B X`(0 + 1), `B Y`(3 + 2), `B Z`(6 + 3),
    `C X`(6 + 1), `C Y`(0 + 2), `C Z`(3 + 3)
}*/
val RoPaScPermutations = mapOf<String, Int>(
    "A X" to 3 + 1, "A Y" to 6 + 2, "A Z" to 0 + 3,
    "B X" to 0 + 1, "B Y" to 3 + 2, "B Z" to 6 + 3,
    "C X" to 6 + 1, "C Y" to 0 + 2, "C Z" to 3 + 3
)

val RoPaScPermElfStrategy = mapOf<String, Int>(
    "A X" to 0 + 3, "A Y" to 3 + 1, "A Z" to 6 + 2,
    "B X" to 0 + 1, "B Y" to 3 + 2, "B Z" to 6 + 3,
    "C X" to 0 + 2, "C Y" to 3 + 3, "C Z" to 6 + 1
)

fun main() {
    fun part1(input: List<String>): Int = input.foldRight(0) { singlePlayResult, acc ->
        acc + RoPaScPermutations.getOrDefault(singlePlayResult, 0)
    }



    fun part2(input: List<String>): Int = input.foldRight(0) { singlePlayResult, acc ->
        acc + RoPaScPermElfStrategy.getOrDefault(singlePlayResult, 0)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}