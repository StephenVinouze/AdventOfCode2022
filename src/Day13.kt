import java.lang.Integer.min

sealed interface PacketData : Comparable<PacketData> {
    data class Single(val value: Int) : PacketData {
        override fun compareTo(other: PacketData): Int =
            when (other) {
                is Single -> this.value - other.value
                is Multiple -> Multiple(listOf(this)).compareTo(other)
            }
    }

    data class Multiple(val values: List<PacketData>) : PacketData {
        override fun compareTo(other: PacketData): Int {
            when (other) {
                is Multiple -> {
                    for (i in 0..min(values.lastIndex, other.values.lastIndex)) {
                        val result = values[i].compareTo(other.values[i])
                        if (result != 0) return result
                    }
                    return values.lastIndex - other.values.lastIndex
                }

                is Single -> return compareTo(Multiple(listOf(other)))
            }
        }
    }
}

fun main() {

    fun String.extract(): List<String> {
        val extraction = mutableListOf<String>()

        var depth = 0
        var currentExtraction = ""

        forEach { char ->
            when (char) {
                '[' -> {
                    depth++
                    currentExtraction += char
                }

                ']' -> {
                    depth--
                    currentExtraction += char
                }

                ',' -> when (depth) {
                    0 -> {
                        extraction += currentExtraction
                        currentExtraction = ""
                    }

                    else -> currentExtraction += char
                }

                else -> currentExtraction += char
            }
        }
        extraction += currentExtraction

        return extraction
    }

    fun String.parse(): PacketData {
        val packet = removeSurrounding("[", "]")
        return when {
            packet.isEmpty() -> PacketData.Multiple(emptyList())
            packet.all { it.isDigit() } -> PacketData.Single(packet.toInt())
            else -> PacketData.Multiple(packet.extract().map { it.parse() })
        }
    }

    fun part1(input: List<String>): Int =
        input
            .filter { it.isNotBlank() }
            .chunked(2)
            .mapIndexed { index, line ->
                val left = line.first().parse()
                val right = line.last().parse()
                if (left < right) index + 1 else 0
            }
            .sum()

    fun part2(input: List<String>): Int {
        val packets = (input + listOf("[[2]]", "[[6]]"))
            .filter { it.isNotBlank() }
            .map { it.parse() }
            .sorted()

        val firstDividerPacket = packets.indexOf(PacketData.Multiple(listOf(PacketData.Single(2)))) + 1
        val secondDividerPacket = packets.indexOf(PacketData.Multiple(listOf(PacketData.Single(6)))) + 1

        return firstDividerPacket * secondDividerPacket
    }

    check(part1(readInput("Day13_test")) == 13)
    check(part2(readInput("Day13_test")) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}