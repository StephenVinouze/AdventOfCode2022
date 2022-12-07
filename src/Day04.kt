fun main() {

    fun String.toSectionRanges(): Pair<IntRange, IntRange> {
        val ranges = this.split(",")
        val firstRangeArray = ranges.first().split("-")
        val lastRangeArray = ranges.last().split("-")
        val firstRange = IntRange(firstRangeArray.first().toInt(), firstRangeArray.last().toInt())
        val lastRange = IntRange(lastRangeArray.first().toInt(), lastRangeArray.last().toInt())
        return Pair(firstRange, lastRange)
    }

    fun part1(input: List<String>): Int =
        input.filter {
            val sectionRanges = it.toSectionRanges()
            val firstRange = sectionRanges.first
            val lastRange = sectionRanges.second

            (firstRange.first <= lastRange.first && firstRange.last >= lastRange.last)||
                    (lastRange.first <= firstRange.first && lastRange.last >= firstRange.last)
        }
            .size

    fun part2(input: List<String>): Int =
        input.filter {
            val sectionRanges = it.toSectionRanges()
            val firstRange = sectionRanges.first
            val lastRange = sectionRanges.second

            firstRange.intersect(lastRange).isNotEmpty()
        }
            .size

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}