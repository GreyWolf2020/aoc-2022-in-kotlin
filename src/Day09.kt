import java.lang.Math.abs

fun main() {
    fun part1(headKnotMotionsData: List<String>): Int {
        val tail = Knot("tail")
        val head = Knot("head")
        tail `join to follow` head

        headKnotMotionsData
            .map { it.split(" ") }
            .forEach {
                val direction = it.first()
                val magnitude = it.last().toInt()
                head
                    .appendMove(direction, magnitude)
                head.execute()
            }

        tail `unjoin so as not to follow` head
        return tail
            .positions
            .distinct()
            .also {
                it.map(::println)
            }
            .size
    }

    fun part2(headKnotMotionsData: List<String>): Int {
        val head = Knot("head")
        val tailOne = Knot("tailOne")
        val tailTwo = Knot("tailTwo")
        val tailThree = Knot("tailThree")
        val tailFour = Knot("tailFour")
        val tailFive = Knot("tailFive")
        val tailSix = Knot("tailSix")
        val tailSeven = Knot("tailSeven")
        val tailEight = Knot("tailEight")
        val tailNine = Knot("tailNine")

        tailOne `join to follow` head
        tailTwo `join to follow` tailOne
        tailThree `join to follow` tailTwo
        tailFour `join to follow` tailThree
        tailFive `join to follow` tailFour
        tailSix `join to follow` tailFive
        tailSeven `join to follow` tailSix
        tailEight `join to follow` tailSeven
        tailNine `join to follow` tailEight

        headKnotMotionsData
            .map { it.split(" ") }
            .forEach {
                val direction = it.first()
                val magnitude = it.last().toInt()
                head
                    .appendMove(direction, magnitude)
            }
        head.execute()

        tailOne `unjoin so as not to follow` head
        tailTwo `unjoin so as not to follow` tailOne
        tailThree `unjoin so as not to follow` tailTwo
        tailFour `unjoin so as not to follow` tailThree
        tailFive `unjoin so as not to follow` tailFour
        tailSix `unjoin so as not to follow` tailFive
        tailSeven `unjoin so as not to follow` tailSix
        tailEight `unjoin so as not to follow` tailSeven
        tailNine `unjoin so as not to follow` tailEight

        return tailNine
            .positions
            .distinct()
            .also {
                it.map(::println)
            }
            .size
    }

    // test if implementation meets criteria from the description, like:
/*    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)

    // testInputTwo is the larger example on the AOC website day 09 challenge of 2022

    val testInputTwo = readInput("Day09_test_two")
    check(part2(testInputTwo) == 36)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))*/
}

typealias Command = () -> Unit

data class Position(val x: Int, val y: Int)

class Knot(
    val name: String,
    var _xPosition: Int = 0,
    var _yPosition: Int = 0
) {
    private val orders = mutableListOf<Command>()
    private val _positions = mutableListOf<Position>()
    val positions
        get() = _positions.toList()
    val followers: HashMap<String, Knot> = HashMap()
    val moveGenerator = fun(
        knot: Knot,
        direction: String,
        qnty: Int
    ): Command {
        return fun() {
            when (direction) {
                "R" -> repeat(qnty) { knot.toRight() }
                "L" -> repeat(qnty) { knot.toLeft() }
                "U" -> repeat(qnty) { knot.toUp() }
                "D" -> repeat(qnty) { knot.toDown() }
                else -> {}
            }
        }
    }

    init {
        appendOrigin()
    }

    fun xPosition(xPosition: Int) { _xPosition = xPosition }
    fun yPosition(yPosition: Int) { _yPosition = yPosition }

    fun appendMove(direction: String, qnty: Int) = apply {
        orders.add(moveGenerator(this, direction, qnty))
    }

    fun appendOrigin() {
        _positions.add(Position(0, 0))
    }

    private fun toLeft() {
        _xPosition--
        moved()
    }
    private fun toRight() {
        _xPosition++
        moved()
    }
    private fun toDown() {
        _yPosition--
        moved()
    }
    private fun toUp() {
        _yPosition++
        moved()
    }

    fun moved() {
        _positions.add(Position(_xPosition, _yPosition))
        for ((_, follower) in followers) {
            follower.follow(this)
        }
    }

    fun execute() {
        while (!orders.isEmpty()) {
            val order = orders.removeAt(0)
            order.invoke()
            println("$name x -> $_xPosition y -> $_yPosition")
        }
    }

    fun follow(leader: Knot) {
        val deltaX = abs(leader._xPosition - this._xPosition)
        val deltaY = abs(leader._yPosition - this._yPosition)

        val xDirection = if (leader._xPosition - _xPosition > 0) "R" else "L"
        val yDirection = if (leader._yPosition - _yPosition > 0) "U" else "D"
        when {
            deltaX > 1 && deltaY > 0 || deltaY > 1 && deltaX > 0 -> {
                moveDiagonally(xDirection, yDirection)
                moved()
            }
            deltaX > 1 -> appendMove(xDirection, deltaX - 1).execute()
            deltaY > 1 -> appendMove(yDirection, deltaY - 1).execute()
        }
    }

    fun moveDiagonally(xDirection: String, yDirection: String) {
        when {
            xDirection == "L" -> _xPosition--
            xDirection == "R" -> _xPosition++
        }
        when {
            yDirection == "U" -> _yPosition++
            yDirection == "D" -> _yPosition--
        }
        /*
        appendMove(xDirection, 1)
            .appendMove(yDirection, 1)
            .execute()*/
    }


}


infix fun Knot.`join to follow`(head: Knot) {
    head.followers.put(this.name, this)
    println("${head.name} has a new follower $name")
}

infix fun Knot.`unjoin so as not to follow`(head: Knot) {
    head.followers.remove(name)
    println("${head.name} has been unfollowed by $name")
}
