fun main() {
    fun part1(input: List<String>): Int {

        val cycleForSignalStrength = mutableListOf<Int>(20, 60, 100, 140, 180, 220)
        val register = Register()
        val clock = Clock().apply { addProcessors(register) }

        val signalStrength = buildList {
            input
                .forEach { instruction ->
                    register
                        .parseInstruction(instruction)

                    while (!register.isInstructionDone) {
                        clock.incrementCycle()
                        if (cycleForSignalStrength.isNotEmpty() && clock.cycle == cycleForSignalStrength.first()) {
                            add(Pair(cycleForSignalStrength.removeFirst(), register.value))
                        }
                    }
                    register.executeInstruction()
                }

        }

        clock.removeProcessor(register)

        return signalStrength
            .sumOf { it.first * it.second }
    }

    fun part2(input: List<String>): Int {
        val register = Register()
        val clock = Clock().apply { addProcessors(register) }

        val crtAllPixels = buildList {
            input
                .forEach { instruction ->
                    register
                        .parseInstruction(instruction)

                    while (!register.isInstructionDone) {
                        val crtCurrentPosition = (clock.cycle + 39 + 1) % 40
                        val sprite = register.value - 1 .. register.value + 1

                        if (crtCurrentPosition in sprite)
                            add("#")
                        else add(".")

                        clock.incrementCycle()
                    }
                    register.executeInstruction()
                }

        }
        clock.removeProcessor(register)


        return crtAllPixels
            .also { drawCrtAllPixels(it) }
            .also { println() }
            .filter { it == "#" }
            .size
    }

//     test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 13140)
//   check(part2(testInput) == 124)
//
//    val input = readInput("Day10")
//    println(part1(input))
//    println(part2(input))
}

interface Processor {
    fun process()
}
data class Clock(var cycle: Int = 0) {
    val processors = mutableListOf<Processor>()
    fun incrementCycle() {
        cycle++
        processors.forEach { processor -> processor.process() }
    }

    fun addProcessors(processor: Processor) {
        if (!processors.contains(processor))
            processors.add(processor)
    }

    fun removeProcessor(processor: Processor) {
        if (!processors.contains(processor))
            processors.remove(processor)
    }
}

class Register(var value: Int = 1) : Processor {
    sealed class Instruction(var cyclesLeft: Int, val valueToAdd: Int) {
        data class Noop(val value: Int = 0): Instruction(1, value)
        data class AddX(val value: Int): Instruction(2, value)

    }

    val isInstructionDone: Boolean
        get() = currentInstruction.cyclesLeft == 0

    lateinit var currentInstruction: Instruction
    override fun process() {
        currentInstruction.cyclesLeft--
    }

    fun executeInstruction() {
        value += currentInstruction.valueToAdd
    }
}

fun Register.parseInstruction(instruction: String) {
    val noopOrAddX = instruction.split(" ")
    when (noopOrAddX.first()) {
        "noop" -> currentInstruction = Register.Instruction.Noop()
        "addx" -> currentInstruction = Register.Instruction.AddX(noopOrAddX.last().toInt())
    }
}

fun drawCrtAllPixels(crtAllPixels: List<String>, screenWidth: Int = 39) {
    crtAllPixels.forEachIndexed { index, pixel ->
        if ((index + 40) % 40 == 0)
            println()
        print(pixel)
    }
}