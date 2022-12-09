import kotlin.math.abs

enum class Direction(val value: String) {
    Up("U"),
    Down("D"),
    Left("L"),
    Right("R")
}

data class Movement(
    val direction: Direction,
    val step: Int
)

data class Coordinates(
    val x: Int,
    val y: Int,
)

fun main() {

    fun List<String>.toMovements(): List<Movement> =
        map { line ->
            val splitMovement = line.split(" ")
            val direction = Direction.values().first { it.value == splitMovement.first() }
            val steps = splitMovement.last().toInt()
            Movement(direction, steps)
        }

    fun computeHeadCoordinates(movements: List<Movement>): List<Coordinates> {
        var currentHeadCoordinates = Coordinates(0, 0)
        val headCoordinates = mutableListOf(currentHeadCoordinates)
        movements.forEach {
            val direction = it.direction
            repeat(it.step) {
                currentHeadCoordinates = when (direction) {
                    Direction.Up -> currentHeadCoordinates.copy(y = currentHeadCoordinates.y + 1)
                    Direction.Down -> currentHeadCoordinates.copy(y = currentHeadCoordinates.y - 1)
                    Direction.Left -> currentHeadCoordinates.copy(x = currentHeadCoordinates.x - 1)
                    Direction.Right -> currentHeadCoordinates.copy(x = currentHeadCoordinates.x + 1)
                }
                headCoordinates.add(currentHeadCoordinates)
            }
        }
        return headCoordinates
    }

    fun computeTailCoordinates(headCoordinates: List<Coordinates>): List<Coordinates> {
        var currentTailCoordinates = Coordinates(0, 0)
        val tailCoordinates = mutableListOf<Coordinates>()
        headCoordinates.forEach { head ->
            val offsetX = if (head.x > currentTailCoordinates.x) -1 else 1
            val offsetY = if (head.y > currentTailCoordinates.y) -1 else 1
            currentTailCoordinates = when {
                abs(currentTailCoordinates.x - head.x) > 1 -> {
                    val x = head.x + offsetX
                    val y = if (abs(currentTailCoordinates.y - head.y) > 1) head.y + offsetY else head.y
                    currentTailCoordinates.copy(x = x, y = y)
                }

                abs(currentTailCoordinates.y - head.y) > 1 -> {
                    val x = if (abs(currentTailCoordinates.x - head.x) > 1) head.x + offsetX else head.x
                    val y = head.y + offsetY
                    currentTailCoordinates.copy(x = x, y = y)
                }

                else -> currentTailCoordinates
            }
            tailCoordinates.add(currentTailCoordinates)
        }

        return tailCoordinates
    }

    fun computeTailTrail(input: List<String>, knotSize: Int): Int {
        var currentHeadCoordinates = computeHeadCoordinates(input.toMovements())
        val tailCoordinatesList = mutableListOf<List<Coordinates>>()
        repeat(knotSize) {
            val tailCoordinates = computeTailCoordinates(currentHeadCoordinates)
            currentHeadCoordinates = tailCoordinates
            tailCoordinatesList.add(tailCoordinates)
        }

        return tailCoordinatesList.last().toSet().size
    }

    fun part1(input: List<String>): Int =
        computeTailTrail(input, knotSize = 1)

    fun part2(input: List<String>): Int =
        computeTailTrail(input, knotSize = 9)

    check(part1(readInput("Day09_test")) == 13)
    check(part2(readInput("Day09_test")) == 1)
    check(part2(readInput("Day09_test2")) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}