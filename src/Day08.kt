import java.lang.StrictMath.max

fun main() {
    fun part1(input: List<String>): Int {
        val treesGraph = AdjacencyList<Int>()
        val verticesOfTreeHeight = treesGraph.parseVertices(input)
        return verticesOfTreeHeight
            .fold(0) { acc, vertex ->
                if (vertex `is max in all directions of` treesGraph)
                    acc + 1
                  else
                    acc
            }
    }

    fun part2(input: List<String>): Int {
        val treesGraph = AdjacencyList<Int>()
        val verticesOfTreeHeight = treesGraph.parseVertices(input)
        return verticesOfTreeHeight
            .fold(Int.MIN_VALUE) { maxScenicScoreSoFar, vertex ->
                max(
                    maxScenicScoreSoFar,
                    vertex `scenic score` treesGraph
                )
            }
    }
    // test if implementation meets criteria from the description, like:
/*    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))*/
}

data class Vertex<T>(
    val index: Int,
    val data: T
)

data class Edge<T>(
    val source: Vertex<T>,
    val destination: Vertex<T>,
    val direction: Graph.EdgeType,
    val weight: Double?
)

interface Graph<T> {

    fun createVertex(data: T): Vertex<T>

    fun addDirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        direction: EdgeType,
        weight: Double?,
    )

    fun addUndirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        direction: EdgeType,
        weight: Double?
    )

    fun add(
        edge: EdgeType,
        source: Vertex<T>,
        destination: Vertex<T>,
        weight: Double?
    )

    fun edges(source: Vertex<T>): Map<EdgeType,Edge<T>>

    fun weight(
        source: Vertex<T>,
        destination: Vertex<T>
    ) : Double?

    enum class EdgeType {
        RIGHT,
        LEFT,
        ABOVE,
        BELOW
    }

}

fun Graph.EdgeType.opposite(): Graph.EdgeType = when(this) {
    Graph.EdgeType.RIGHT -> Graph.EdgeType.LEFT
    Graph.EdgeType.LEFT -> Graph.EdgeType.RIGHT
    Graph.EdgeType.ABOVE -> Graph.EdgeType.BELOW
    Graph.EdgeType.BELOW -> Graph.EdgeType.ABOVE
}

class AdjacencyList <T> : Graph<T> {
    private val adjacencies: HashMap<Vertex<T>, MutableMap<Graph.EdgeType,Edge<T>>> = HashMap()

    override fun createVertex(data: T): Vertex<T> {
        val vertex = Vertex(adjacencies.count(), data)
        adjacencies[vertex] = mutableMapOf()
        return vertex
    }


    override fun addDirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        direction: Graph.EdgeType,
        weight: Double?
    ) {
        val edge = Edge(source, destination, direction, weight)
        adjacencies[source]?.put(edge.direction, edge)
    }

    override fun addUndirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        direction: Graph.EdgeType,
        weight: Double?
    ) {
        addDirectedEdge(source, destination, direction, weight)
        addDirectedEdge(destination, source, direction.opposite(), weight)
    }


    override fun add(edge: Graph.EdgeType, source: Vertex<T>, destination: Vertex<T>, weight: Double?) = addUndirectedEdge(source, destination, edge, weight)

    override fun edges(source: Vertex<T>): Map<Graph.EdgeType, Edge<T>>  = adjacencies[source] ?: mutableMapOf<Graph.EdgeType, Edge<T>>()

    override fun weight(source: Vertex<T>, destination: Vertex<T>): Double? {
        return edges(source).map { it.value }.firstOrNull { it.destination == destination }?.weight
    }


    override fun toString(): String {
        return buildString { // 1
            adjacencies.forEach { (vertex, edges) -> // 2
                val edgeString = edges
                    .map {Pair(it.key,it.value)}.joinToString { it.second.destination.data.toString() + " " + it.first.toString()} // 3
                append("${vertex.data} ---> [ $edgeString ]\n") // 4
            }
        }
    }
}

fun AdjacencyList<Int>.parseVertices(verticesData: List<String>): List<Vertex<Int>> {
    val verticesDataMatrix = verticesData.map { verticesDataRow ->
        verticesDataRow
            .chunked(1)
            .map { treeHeight ->
                treeHeight.toInt()
            }
    }

    val verticesDataRow = verticesDataMatrix.first().size
    val verticesDataColumn = verticesDataMatrix.size
    val verticesLastRow = verticesDataRow * verticesDataColumn + 1 - verticesDataRow .. verticesDataRow * verticesDataColumn

    val verticesList = verticesDataMatrix
        .flatten()
        .map {
            createVertex(it)
        }
    for (vertex in verticesList) {
        val vertexPosition = vertex.index + 1
        if (vertexPosition % verticesDataRow != 0) {
            add(Graph.EdgeType.RIGHT, vertex, verticesList[vertex.index + 1], null)
        }
        if (vertexPosition !in verticesLastRow) {
            add(Graph.EdgeType.BELOW, vertex, verticesList[vertex.index + verticesDataRow], null)
        }
    }
    return verticesList
}


fun Vertex<Int>.isMax(graph: Graph<Int>, direction: Graph.EdgeType): Boolean {
    tailrec fun go(direction: Graph.EdgeType, vert: Vertex< Int>, maxSoFar: Int): Int {
        val destinationVertex = graph.edges(vert)[direction]?.destination
        return if (destinationVertex == null)
            maxSoFar
        else go(direction, destinationVertex, max(maxSoFar, destinationVertex.data))
    }
    val isMaximum by lazy {
        this.data > go(direction, this, Integer.MIN_VALUE)
    }
    return isMaximum
}

infix fun Vertex<Int>.`is max in all directions of`(graph: Graph<Int>): Boolean = isMax(graph, Graph.EdgeType.RIGHT) ||
            isMax(graph, Graph.EdgeType.LEFT) ||
            isMax(graph, Graph.EdgeType.ABOVE) ||
            isMax(graph, Graph.EdgeType.BELOW)

fun Vertex<Int>.countToImmediateMax(graph: Graph<Int>, direction: Graph.EdgeType): Int {
    fun Int.go(direction: Graph.EdgeType, vert: Vertex<Int>, smallerImmediateVerticesCount: Int): Int {
        val destinationVertex = graph.edges(vert)[direction]?.destination
        return when {
            destinationVertex == null -> smallerImmediateVerticesCount
            destinationVertex.data >= this -> smallerImmediateVerticesCount + 1
            else -> go(direction, destinationVertex, smallerImmediateVerticesCount + 1)
        }

    }
    return data.go(direction, this, 0)
}

infix fun Vertex<Int>.`scenic score`(graph: Graph<Int>): Int = countToImmediateMax(graph, Graph.EdgeType.RIGHT) *
        countToImmediateMax(graph, Graph.EdgeType.LEFT) *
        countToImmediateMax(graph, Graph.EdgeType.ABOVE) *
        countToImmediateMax(graph, Graph.EdgeType.BELOW)