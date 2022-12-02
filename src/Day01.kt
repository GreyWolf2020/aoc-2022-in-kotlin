import java.lang.Integer.max

fun main() {
    fun part1(input: List<String>): Int {
        val sumIndex = 0
        val maxSumIndex = 1
        val zeroCalorie = 0
        return input.map {
            it.toIntOrNull()
        }.foldRight(intArrayOf(0, 0)) { calorieOrNull, acc ->
            calorieOrNull ?.let { calorie ->
                intArrayOf(acc[sumIndex] + calorie, acc[maxSumIndex])
            } ?: run {
                intArrayOf(zeroCalorie, max(acc[maxSumIndex], acc[sumIndex]))
            }
        }[maxSumIndex]
    }


    fun part2(input: List<String>): Int {
        val sumIndex = 0
        val highSumIndex = 1
        val highHighSumIndex = 2
        val highHighHighSumIndex = 3
        val zeroCalorie = 0

        return input.map {
            it.toIntOrNull()
        }.foldRight(intArrayOf(0, 0, 0, 0)) { calorieOrNull, acc ->
            calorieOrNull?.let { calorie ->
                intArrayOf(acc[sumIndex] + calorie, acc[highSumIndex], acc[highHighSumIndex], acc[highHighHighSumIndex])
            } ?: run {
                when {
                    acc[sumIndex] > acc[highSumIndex] && acc[sumIndex] < acc[highHighSumIndex] -> intArrayOf(zeroCalorie, acc[sumIndex], acc[highHighSumIndex], acc[highHighHighSumIndex])
                    acc[sumIndex] > acc[highHighSumIndex] && acc[sumIndex] < acc[highHighHighSumIndex] -> intArrayOf(zeroCalorie, acc[highHighSumIndex], acc[sumIndex], acc[highHighHighSumIndex])
                    acc[sumIndex] > acc[3] -> intArrayOf(zeroCalorie, acc[highHighSumIndex], acc[highHighHighSumIndex], acc[sumIndex])
                    else -> intArrayOf(zeroCalorie, acc[highSumIndex], acc[highHighSumIndex], acc[highHighHighSumIndex])
                }
            }
        }.drop(1).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    check(part2(testInput) == 45000)



    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
