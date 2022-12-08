const val TOTALDISKSPACE = 70000000
const val NEEDEDUNUSEDSPACE = 30000000

fun main() {

    fun part1(input: String): Int = FileSystem().run {
        parseInstructions(input)
        getListOfDirNSizes()
            .map { it.second }
            .filter { it < 100000 }
            .sum()
    }

    fun part2(input: String): Pair<String, Int> = FileSystem().run {
        parseInstructions(input)
        val listOfDirNSizes = getListOfDirNSizes()
        val totalUsedSize = getListOfDirNSizes().maxOf { it.second }
        val totalUnUsedSpace = TOTALDISKSPACE - totalUsedSize
        listOfDirNSizes
            .filter { dirNSize ->
                val (_, size) = dirNSize
                size  >= NEEDEDUNUSEDSPACE - totalUnUsedSpace
            }
            .minBy { it.second }

    }

    // test if implementation meets criteria from the description, like:
    // table test of part1
//    val testInput = readInputAsString("Day07_test")
//    check(part1(testInput) == 95437)
//    check(part2(testInput) == Pair("d", 24933642))
//
//    val input = readInputAsString("Day07")
//    println(part1(input))
//    println(part2(input))
}

sealed class FileType (val name: String, val parent: FileType.Dir?) {

    class Dir(name: String, parent: FileType.Dir?, val children: HashMap<String, FileType> = hashMapOf()) : FileType(name, parent) {
        override fun getTheSize(): Int = children
            .map {
                it.value
            }
            .fold(0) { sizes, children ->
                sizes + children.getTheSize()
            }
    }

    class MyFile(name: String, parent: FileType.Dir, val size: Int) : FileType(name, parent) {
        override fun getTheSize(): Int = size
    }

    abstract fun getTheSize(): Int
}

fun FileType.Dir.getDirNSize(list: MutableList<Pair<String, Int>>) {
    children.map { it.value }
        .forEach {
            if (it is FileType.Dir)
                it.getDirNSize(list)
        }
    val size = getTheSize()
    list.add(
        Pair(name, size)
    )
}

class FileSystem {
    private val rootDir = FileType.Dir("/", null)
    private var current = rootDir

    fun changeDirectory(param: String) {
        current = when(param) {
            ".." -> current.parent ?: rootDir
            "/" -> rootDir
            else -> current
                .children
                .getOrElse(param){
                    val newChild = FileType.Dir(param, current)
                    current.children.put(param, newChild)
                    newChild
                } as FileType.Dir
        }
    }

    fun listing(files: List<String>) {
        files
            .map {
                it.split(" ")
            }
            .fold(current) { cur, file ->
                when {
                    file.first() == "dir" -> cur.children.put(file.last(), FileType.Dir(file.last(), cur))
                    else -> cur.children.put(file.last(), FileType.MyFile(file.last(), cur, file.first().toInt()))
                }
                cur
            }
    }

    fun parseInstructions(instructions: String) {
        instructions
            .split("$")
            .map {
                it.trim()
            }
            .forEach {
                val commandNParamsData = it
                    .lines()
                val command = commandNParamsData
                    .first()
                    .split(" ")
                when (command.first()) {
                    "cd" -> changeDirectory(command.last())
                    "ls" -> listing(commandNParamsData
                        .drop(1) // remove any string that follows the command ls, which is blank in this case
                    )
                    else -> {}
                }
            }
    }

    fun getListOfDirNSizes(): MutableList<Pair<String, Int>> = mutableListOf<Pair<String, Int>>().apply {
        rootDir.getDirNSize(this)
    }

}

