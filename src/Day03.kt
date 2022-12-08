fun main() {
    fun itemPriority(c: Char): Int = when  {
        c.isLowerCase() -> c.code - 96
        c.isUpperCase() -> c.code - 64 + 26
        else -> 0
    }
    fun part1(input: List<String>): Int = input.foldRight(0) { rucksack, prioritySum ->
        rucksack
            .chunked(rucksack.length / 2)
            .map {
                it.toSet()
            }
            .intersection()
            .map(::itemPriority)
            .sum() + prioritySum
    }



    fun part2(input: List<String>): Int = input
            .windowed(3, 3)
            .foldRight(0) { threeRucksacks, prioritySum ->
                threeRucksacks
                    .map { it.toSet() }
                    .intersection()
                    .map(::itemPriority)
                    .sum() + prioritySum
            }


    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day03_test")
//    check(part1(testInput) == 157)
//    check(part2(testInput) == 70)
//
//    val input = readInput("Day03")
//    println(part1(input))
//    println(part2(input))
}
//
//fun List<Set<Char>>.intersection() = reduce { element, acc ->
//    element.intersect(acc)
//}
private fun <A> List<Set<A>>.intersection(): Set<A> = reduce { element, acc ->
    element.intersect(acc)
}