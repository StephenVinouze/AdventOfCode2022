import kotlin.math.pow

fun main() {

    fun String.fromSnafu(): Long =
        reversed()
            .mapIndexed { index, digit ->
                when (digit) {
                    '=' -> -2
                    '-' -> -1
                    else -> digit.digitToInt()
                } * 5.0.pow(index)
            }
            .sumOf { it.toLong() }

    fun Long.toSnafu(): String {
        var power = 1L
        var remainder = this
        var snafu = ""

        while (remainder != 0L) {
            val snafuDigit = when (remainder % (5L * power)) {
                0L -> '0'
                1L * power -> '1'.also { remainder -= 1 * power }
                2L * power -> '2'.also { remainder -= 2 * power }
                3L * power -> '='.also { remainder += 2 * power }
                4L * power -> '-'.also { remainder += 1 * power }
                else -> throw IllegalArgumentException()
            }
            power *= 5
            snafu += snafuDigit
        }

        return snafu.reversed()
    }

    fun part1(input: List<String>): String =
        input
            .map { it.fromSnafu() }
            .sumOf { it }
            .toSnafu()

    fun part2(input: List<String>): Int {


        return 0
    }

    check(part1(readInput("Day25_test")) == "2=-1=0")
    //check(part2(readInput("Day25_test")) == 20)

    val input = readInput("Day25")
    println(part1(input))
    //println(part2(input))
}