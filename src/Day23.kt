fun Coordinates.neighborsIn(direction: Direction): List<Coordinates> =
    when (direction) {
        Direction.Up -> listOf(
            copy(x = x - 1, y = y - 1),
            copy(x = x, y = y - 1),
            copy(x = x + 1, y = y - 1),
        )

        Direction.Down -> listOf(
            copy(x = x - 1, y = y + 1),
            copy(x = x, y = y + 1),
            copy(x = x + 1, y = y + 1),
        )

        Direction.Left -> listOf(
            copy(x = x - 1, y = y - 1),
            copy(x = x - 1, y = y),
            copy(x = x - 1, y = y + 1),
        )

        Direction.Right -> listOf(
            copy(x = x + 1, y = y - 1),
            copy(x = x + 1, y = y),
            copy(x = x + 1, y = y + 1),
        )
    }

fun Coordinates.hasNeighborsIn(coordinates: List<Coordinates>, direction: Direction? = null): Boolean =
    (direction?.let { neighborsIn(it) } ?: Direction.values().flatMap { neighborsIn(it) })
        .any { it in coordinates }

fun Direction.next(): Direction =
    when (this) {
        Direction.Up -> Direction.Down
        Direction.Down -> Direction.Left
        Direction.Left -> Direction.Right
        Direction.Right -> Direction.Up
    }

fun main() {

    fun List<String>.toElvesPositions(): List<Coordinates> =
        flatMapIndexed { columnIndex, line ->
            line.toCharArray().toList().mapIndexedNotNull { rowIndex, char ->
                if (char == '#') Coordinates(x = rowIndex, y = columnIndex) else null
            }
        }
            .toMutableList()

    fun nextElvesPositions(elvesPositions: List<Coordinates>, direction: Direction): List<Pair<Coordinates, Int>> =
        elvesPositions.mapIndexedNotNull { index, elf ->
            if (elf.hasNeighborsIn(elvesPositions)) {
                var currentDirection = direction
                repeat(4) {
                    if (!elf.hasNeighborsIn(elvesPositions, currentDirection)) {
                        return@mapIndexedNotNull when (currentDirection) {
                            Direction.Up -> elf.copy(y = elf.y - 1)
                            Direction.Down -> elf.copy(y = elf.y + 1)
                            Direction.Left -> elf.copy(x = elf.x - 1)
                            Direction.Right -> elf.copy(x = elf.x + 1)
                        } to index
                    }
                    currentDirection = currentDirection.next()
                }
            }
            null
        }

    fun part1(input: List<String>): Int {
        var emptyGrounds = 0
        var direction = Direction.Up
        val elvesPositions = input.toElvesPositions().toMutableList()

        repeat(10) {
            val nextElvesPositionsAndIndexes = nextElvesPositions(elvesPositions, direction)

            // Assign new positions
            nextElvesPositionsAndIndexes.forEachIndexed { currentIndex, (nextCoordinates, index) ->
                if (nextCoordinates !in nextElvesPositionsAndIndexes
                        .map { it.first }
                        .filterIndexed { index, _ -> index != currentIndex }
                ) {
                    elvesPositions[index] = nextCoordinates
                }
            }

            // Change direction for next round
            direction = direction.next()
        }

        (elvesPositions.minOf { it.y }..elvesPositions.maxOf { it.y }).forEach { y ->
            (elvesPositions.minOf { it.x }..elvesPositions.maxOf { it.x }).forEach { x ->
                if (Coordinates(x, y) !in elvesPositions) emptyGrounds++
            }
        }
        return emptyGrounds
    }

    fun part2(input: List<String>): Int {
        var rounds = 0
        var direction = Direction.Up
        val elvesPositions = input.toElvesPositions().toMutableList()
        var nextElvesPositions: MutableList<Coordinates>

        while (true) {
            rounds++

            val nextElvesPositionsAndIndexes = nextElvesPositions(elvesPositions, direction)

            nextElvesPositions = nextElvesPositionsAndIndexes.map { it.first }.toMutableList()
            if (nextElvesPositions.isEmpty()) {
                break
            }

            // Assign new positions
            nextElvesPositionsAndIndexes.forEachIndexed { currentIndex, (nextCoordinates, index) ->
                if (nextCoordinates !in nextElvesPositions
                        .filterIndexed { index, _ -> index != currentIndex }
                ) {
                    elvesPositions[index] = nextCoordinates
                }
            }

            // Change direction for next round
            direction = direction.next()
        }

        return rounds
    }

    check(part1(readInput("Day23_test")) == 110)
    check(part2(readInput("Day23_test")) == 20)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}