import java.lang.IllegalArgumentException

enum class Shape(
    val value: Int
) {
    Rock(1),
    Paper(2),
    Scissors(3),
}

enum class Result(val score: Int) {
    Loss(0),
    Draw(3),
    Win(6),
}

fun main() {

    fun roundScorePart1(round: String): Int {
        val shapes = round.split(" ").take(2)
        val opponentShape = when (shapes.first()) {
            "A" -> Shape.Rock
            "B" -> Shape.Paper
            "C" -> Shape.Scissors
            else -> throw IllegalArgumentException("Unknown opponent shape")
        }
        val myShape = when (shapes.last()) {
            "X" -> Shape.Rock
            "Y" -> Shape.Paper
            "Z" -> Shape.Scissors
            else -> throw IllegalArgumentException("Unknown shape for myself")
        }

        val result = when (opponentShape) {
            Shape.Rock -> when (myShape) {
                Shape.Rock -> Result.Draw
                Shape.Paper -> Result.Win
                Shape.Scissors -> Result.Loss
            }
            Shape.Paper -> when (myShape) {
                Shape.Rock -> Result.Loss
                Shape.Paper -> Result.Draw
                Shape.Scissors -> Result.Win
            }
            Shape.Scissors -> when (myShape) {
                Shape.Rock -> Result.Win
                Shape.Paper -> Result.Loss
                Shape.Scissors -> Result.Draw
            }
        }

        return result.score + myShape.value
    }

    fun roundScorePart2(round: String): Int {
        val shapes = round.split(" ").take(2)
        val opponentShape = when (shapes.first()) {
            "A" -> Shape.Rock
            "B" -> Shape.Paper
            "C" -> Shape.Scissors
            else -> throw IllegalArgumentException("Unknown opponent shape")
        }
        val result = when (shapes.last()) {
            "X" -> Result.Loss
            "Y" -> Result.Draw
            "Z" -> Result.Win
            else -> throw IllegalArgumentException("Unknown result for myself")
        }

        val myShape = when (opponentShape) {
            Shape.Rock -> when (result) {
                Result.Loss -> Shape.Scissors
                Result.Draw -> Shape.Rock
                Result.Win -> Shape.Paper
            }
            Shape.Paper -> when (result) {
                Result.Loss -> Shape.Rock
                Result.Draw -> Shape.Paper
                Result.Win -> Shape.Scissors
            }
            Shape.Scissors -> when (result) {
                Result.Loss -> Shape.Paper
                Result.Draw -> Shape.Scissors
                Result.Win -> Shape.Rock
            }
        }

        return result.score + myShape.value
    }

    fun part1(input: List<String>): Int =
        input.sumOf { roundScorePart1(it) }

    fun part2(input: List<String>): Int =
        input.sumOf { roundScorePart2(it) }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}