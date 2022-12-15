import kotlin.math.abs

fun Coordinates.distance(to: Coordinates): Int =
    abs(x - to.x) + abs(y - to.y)

data class Sensor(
    val sensorCoordinates: Coordinates,
    val beaconCoordinates: Coordinates,
) {
    val distance: Int
        get() = sensorCoordinates.distance(beaconCoordinates)

    fun withinRange(coordinates: Coordinates): Boolean =
        sensorCoordinates.distance(coordinates) <= distance
}

fun main() {

    fun MatchResult.toCoordinates(): Coordinates {
        val coords = value.split(",")
        return Coordinates(
            x = coords.first().split("=").last().trim().toInt(),
            y = coords.last().split("=").last().trim().toInt(),
        )
    }

    fun parse(input: List<String>): List<Sensor> =
        input.map { line ->
            val regex = "x=-?\\d+, y=-?\\d+".toRegex()
            val matches = regex.findAll(line)
            Sensor(
                sensorCoordinates = matches.first().toCoordinates(),
                beaconCoordinates = matches.last().toCoordinates(),
            )
        }

    fun rowCoverage(
        sensor: Sensor,
        x: Int,
        y: Int,
        distance: Int
    ): Coordinates? {
        val relativeDistance = abs(sensor.sensorCoordinates.x - x) + abs(sensor.sensorCoordinates.y - y)
        return if (relativeDistance <= distance && Coordinates(x, y) != sensor.beaconCoordinates) {
            Coordinates(x = x, y = y)
        } else null
    }

    fun buildInnerCoverage(
        coverage: MutableSet<Coordinates>,
        sensor: Sensor,
        inspectedY: Int? = null
    ) {
        val distance = sensor.distance
        val sensorCoordinates = sensor.sensorCoordinates

        (sensorCoordinates.x - distance..sensorCoordinates.x + distance).forEach { x ->
            if (inspectedY != null) {
                rowCoverage(sensor, x, inspectedY, distance)?.let { coverage += it }
            } else {
                (sensorCoordinates.y - distance..sensorCoordinates.y + distance).forEach { y ->
                    rowCoverage(sensor, x, y, distance)?.let { coverage += it }
                }
            }
        }
    }

    fun buildOuterCoverage(
        sensor: Sensor,
    ): List<Coordinates> {
        val coverage = mutableListOf<Coordinates>()
        val sensorCoordinates = sensor.sensorCoordinates
        val distance = sensor.distance + 1

        (-distance..distance).forEach { x ->
            coverage += Coordinates(sensorCoordinates.x + x, sensorCoordinates.y + x + distance)
        }

        return coverage
    }

    fun part1(input: List<String>, y: Int): Int {
        val sensors = parse(input)
        val totalCoverage = mutableSetOf<Coordinates>()

        sensors
            .forEach { sensor ->
                buildInnerCoverage(totalCoverage, sensor, y)
            }

        return totalCoverage.count { it.y == y }
    }

    fun part2(input: List<String>, x: Int): Long {
        val sensors = parse(input)
        val totalRange = (0..x)

        return sensors
            .firstNotNullOf { sensor ->
                buildOuterCoverage(sensor)
                    .filter { it.x in totalRange && it.y in totalRange }
                    .firstOrNull { outerCoordinate -> sensors.none { sensor -> sensor.withinRange(outerCoordinate) } }
            }
            .let { it.x * 4000000L + it.y }
    }

    check(part1(readInput("Day15_test"), 10) == 26)
    check(part2(readInput("Day15_test"), 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}