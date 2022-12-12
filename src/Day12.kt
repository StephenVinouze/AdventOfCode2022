data class Square(
    val x: Int,
    val y: Int,
    val height: Int,
    val isStart: Boolean,
    val isEnd: Boolean,
)

fun main() {

    fun Char.toHeight(): Int =
        when (this) {
            'S' -> 0
            'E' -> 26
            else -> this - 'a'
        }

    fun List<String>.toSquares(): List<List<Square>> =
        mapIndexed { columnIndex, line ->
            line.toCharArray().toList().mapIndexed { rowIndex, char ->
                Square(
                    x = rowIndex,
                    y = columnIndex,
                    height = char.toHeight(),
                    isStart = char == 'S',
                    isEnd = char == 'E',
                )
            }
        }

    fun breadthFirstSearch(
        squares: List<List<Square>>,
        startSquare: Square,
    ): Int {
        val visitedSquares = mutableSetOf<Square>()
        val flattenSquares = squares.flatten()
        val endSquare = flattenSquares.first { it.isEnd }
        val toVisitSquares = mutableListOf(startSquare)
        val costs = Array(squares.size) { IntArray(squares.first().size) { 100000 } }
        costs[startSquare.y][startSquare.x] = 0

        while (toVisitSquares.isNotEmpty()) {
            val visitedSquare = toVisitSquares.removeFirst()

            listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
                .map { (diffY, diffX) -> (visitedSquare.y + diffY) to (visitedSquare.x + diffX) }
                .forEach { (neighborY, neighborX) ->
                    flattenSquares
                        .firstOrNull { it.y == neighborY && it.x == neighborX }
                        ?.let { neighborSquare ->
                            if (neighborSquare.height <= visitedSquare.height + 1) {
                                costs[neighborY][neighborX] = minOf(
                                    costs[neighborY][neighborX],
                                    costs[visitedSquare.y][visitedSquare.x] + 1
                                )

                                if (neighborSquare !in toVisitSquares && neighborSquare !in visitedSquares) {
                                    toVisitSquares.add(neighborSquare)
                                }
                            }
                        }
                }

            visitedSquares.add(visitedSquare)
        }

        return costs[endSquare.y][endSquare.x]
    }

    fun part1(input: List<String>): Int {
        val squares = input.toSquares()
        val startSquare = squares.flatten().first { it.isStart }

        return breadthFirstSearch(squares, startSquare)
    }

    fun part2(input: List<String>): Int {
        val squares = input.toSquares()
        val startSquares = squares.flatten().filter { it.height == 0 }

        return startSquares.minOf { breadthFirstSearch(squares, it) }
    }

    check(part1(readInput("Day12_test")) == 31)
    check(part2(readInput("Day12_test")) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}