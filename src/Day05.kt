import java.io.File
import java.util.Stack

typealias Stacks<E> = List<Stack<E>>
typealias StacksOfChar = Stacks<Char>

fun main() {

    fun part1(input: String) = findCratesOnTopOfAllStacks(
        input,
        StacksOfChar::moveOneStackAtTime
    )

    fun part2(input: String) = findCratesOnTopOfAllStacks(
        input,
        StacksOfChar::moveAllStacksOnce
    )

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("Day05_test")
    check(part1(testInput) == "CMZ")

    check(part2(testInput) == "MCD")

    val input = readInputAsString("Day05")
    println(part1(input))
    println(part2(input))
}



fun findCratesOnTopOfAllStacks(
    input: String,
    moveOneOrAllAtATime: StacksOfChar.(Int, Int, Int) -> StacksOfChar
): String {
    val (stackData, proceduresData) = input
        .split("\n\r\n") // separate the information of stacks from that of procedures
    val stackSize = stackData
        .toIntStackSize() // get the last number of the stacks which is in turn the size of stacks
    val listOfStacks = buildList { repeat(stackSize) { add(Stack<Char>()) } }
    stackData
        .lines() // get the cranes at each level of the stacks from the top
        .dropLast(2) // the line with the numbering of the stacks
        .map {
            it
                .chunked(4)
                .map { it[1] }
        } // remove the clutter such as the space and the brackets, take only the character content of the cranes
        .foldRight(listOfStacks) { cranes, stacks ->
            cranes.forEachIndexed { craneStackNum, crane ->
                if (crane.toString().isNotBlank()) {
                    stacks[craneStackNum].push(crane) // push the crates into the list of stacks
                }
            } // build the initial state of the states
            stacks
        }
    proceduresData
        .lines() // get the lines of the procedures
        .map {
            it.split(" ")
        }
        .fold(listOfStacks) { stacks, procedure ->
            stacks
                .moveOneOrAllAtATime(
                    procedure[1].toInt(), // the number of cranes to move
                    procedure[3].toInt() - 1, // the stack to move the cranes from
                    procedure[5].toInt() - 1 // the stack to move the cranes to
                )
        }
    return buildString { listOfStacks.forEach { if (it.isEmpty()) append(" ") else append(it.pop()) } }
}

private fun <E> Stacks<E>.moveOneStackAtTime(crates: Int, fromStack: Int, toStack: Int): Stacks<E> = apply {
    repeat(crates) {
        if (this[fromStack].isNotEmpty()) {
            this[toStack].push(this[fromStack].pop())
        } else this.map { println(it) }
    }
}

private fun <E> Stacks<E>.moveAllStacksOnce(crates: Int, fromStack: Int, toStack: Int): Stacks<E> = apply {
    val tempStack = Stack<E>()
    repeat(crates) {
        if (this[fromStack].isNotEmpty()) {
            tempStack.push(this[fromStack].pop())
        } else this.map { println(it) }
    }
    repeat(crates) {
        if (tempStack.isNotEmpty()) {
            this[toStack].push(tempStack.pop())
        }
    }
}

private fun String.toIntStackSize() = split("\n")
    .last()// get the labels of all the stack
    .split(" ")// get the list of each stack's label
    .last() // get the last stack's label
    .trim() // remove any characters around the label
    .toInt() // return the label as an Int

