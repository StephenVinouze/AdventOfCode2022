fun main() {

    fun packetIndex(packet: String, uniqueChar: Int): Int {
        val marker =  packet
            .windowed(uniqueChar)
            .first { it.all(hashSetOf<Char>()::add) }
        return packet.indexOf(marker) + marker.length
    }

    fun part1(input: List<String>): Int =
        packetIndex(input.first(), 4)

    fun part2(input: List<String>): Int =
        packetIndex(input.first(), 14)

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}