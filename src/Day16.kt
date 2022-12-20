data class Node(
    val name: String,
    val value: Int,
    val nodes: List<String>,
)

fun main() {

    fun List<String>.toNodes(): List<Node> =
        map { line ->
            Node(
                name = line
                    .substringAfter("Valve ")
                    .take(2),
                value = line
                    .split(";")
                    .first()
                    .split("=")
                    .last()
                    .toInt(),
                nodes = line
                    .split(";")
                    .last()
                    .substringAfter(" tunnels lead to valves ")
                    .split(",")
                    .map { it.trim() },
            )
        }

    fun part1(input: List<String>): Int {
        println(input.toNodes())

        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    check(part1(readInput("Day16_test")) == 1651)
    //check(part2(readInput("Day16_test")) == 56000011L)

    val input = readInput("Day16")
    //println(part1(input))
    //println(part2(input))
}