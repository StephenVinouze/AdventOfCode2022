fun main() {

    fun List<String>.toRocks(): Set<Coordinates> {
        val rocksCoordinates = this.map {
            it.split(" -> ").map { coordinates ->
                coordinates.split(",").let { point ->
                    Coordinates(x = point.first().toInt(), y = point.last().toInt())
                }
            }
        }

        val allRocksCoordinates = mutableSetOf<Coordinates>()

        rocksCoordinates.forEach { rocksLineCoordinates ->
            rocksLineCoordinates
                .windowed(2)
                .forEach {
                    val startRock = it.first()
                    val endRock = it.last()

                    allRocksCoordinates += when {
                        startRock.x == endRock.x ->
                            IntRange(minOf(startRock.y, endRock.y), maxOf(startRock.y, endRock.y))
                                .map { y -> Coordinates(startRock.x, y) }

                        startRock.y == endRock.y ->
                            IntRange(minOf(startRock.x, endRock.x), maxOf(startRock.x, endRock.x))
                                .map { x -> Coordinates(x, startRock.y) }

                        else -> throw IllegalStateException("Cannot draw line if x or y don't match!")
                    }
                }
        }

        return allRocksCoordinates
    }

    fun part1(input: List<String>): Int {
        val startSandCoordinates = Coordinates(x = 500, y = 0)
        var currentSandCoordinates = startSandCoordinates
        var restingSandCount = 0
        val rocks = input.toRocks()
        val sands = mutableSetOf<Coordinates>()
        val depth = rocks.maxOf { it.y }

        while (true) {
            val straightDownSandCoordinates = currentSandCoordinates.copy(y = currentSandCoordinates.y + 1)
            val leftDownSandCoordinates = currentSandCoordinates.copy(x = currentSandCoordinates.x - 1, y = currentSandCoordinates.y + 1)
            val rightDownSandCoordinates = currentSandCoordinates.copy(x = currentSandCoordinates.x + 1, y = currentSandCoordinates.y + 1)
            val filledCoordinates = rocks + sands
            currentSandCoordinates = when {
                straightDownSandCoordinates !in filledCoordinates -> straightDownSandCoordinates
                leftDownSandCoordinates !in filledCoordinates -> leftDownSandCoordinates
                rightDownSandCoordinates !in filledCoordinates -> rightDownSandCoordinates
                else -> {
                    restingSandCount++
                    sands += currentSandCoordinates
                    startSandCoordinates
                }
            }

            if (currentSandCoordinates.y >= depth) break
        }

        return restingSandCount
    }

    fun part2(input: List<String>): Int {
        val startSandCoordinates = Coordinates(x = 500, y = 0)
        var currentSandCoordinates = startSandCoordinates
        var restingSandCount = 0
        val rocks = input.toRocks()
        val sands = mutableSetOf<Coordinates>()
        val depth = rocks.maxOf { it.y } + 2

        while (true) {
            val straightDownSandCoordinates = currentSandCoordinates.copy(y = currentSandCoordinates.y + 1)
            val leftDownSandCoordinates = currentSandCoordinates.copy(x = currentSandCoordinates.x - 1, y = currentSandCoordinates.y + 1)
            val rightDownSandCoordinates = currentSandCoordinates.copy(x = currentSandCoordinates.x + 1, y = currentSandCoordinates.y + 1)
            val filledCoordinates = rocks + sands
            val hasReachedBottom = straightDownSandCoordinates.y == depth
            currentSandCoordinates = when {
                !hasReachedBottom && straightDownSandCoordinates !in filledCoordinates -> straightDownSandCoordinates
                !hasReachedBottom && leftDownSandCoordinates !in filledCoordinates -> leftDownSandCoordinates
                !hasReachedBottom && rightDownSandCoordinates !in filledCoordinates -> rightDownSandCoordinates
                else -> {
                    restingSandCount++
                    sands += currentSandCoordinates
                    if (currentSandCoordinates == startSandCoordinates) break
                    startSandCoordinates
                }
            }
        }

        return restingSandCount
    }

    check(part1(readInput("Day14_test")) == 24)
    check(part2(readInput("Day14_test")) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}