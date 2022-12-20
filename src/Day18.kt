data class Cube(
    val x: Int,
    val y: Int,
    val z: Int,
)

fun main() {

    fun List<String>.toCubes(): List<Cube> =
        map {
            val split = it.split(",")
            Cube(split[0].toInt(), split[1].toInt(), split[2].toInt())
        }

    fun computeUncoveredSides(cubes: List<Cube>): Int =
        cubes.fold(0) { acc, currentCube ->
            var uncoveredSides = acc
            val otherCubes = cubes.filterNot { it == currentCube }
            if (otherCubes.none { cube -> cube.x == currentCube.x - 1 && cube.y == currentCube.y && cube.z == currentCube.z }) uncoveredSides++
            if (otherCubes.none { cube -> cube.x == currentCube.x + 1 && cube.y == currentCube.y && cube.z == currentCube.z }) uncoveredSides++
            if (otherCubes.none { cube -> cube.y == currentCube.y - 1 && cube.x == currentCube.x && cube.z == currentCube.z }) uncoveredSides++
            if (otherCubes.none { cube -> cube.y == currentCube.y + 1 && cube.x == currentCube.x && cube.z == currentCube.z }) uncoveredSides++
            if (otherCubes.none { cube -> cube.z == currentCube.z - 1 && cube.x == currentCube.x && cube.y == currentCube.y }) uncoveredSides++
            if (otherCubes.none { cube -> cube.z == currentCube.z + 1 && cube.x == currentCube.x && cube.y == currentCube.y }) uncoveredSides++
            uncoveredSides
        }

    fun part1(input: List<String>): Int =
        computeUncoveredSides(input.toCubes())

    fun part2(input: List<String>): Int {
        return 0
    }

    check(part1(readInput("Day18_test")) == 64)
    //check(part2(readInput("Day18_test")) == 58)

    val input = readInput("Day18")
    println(part1(input))
    //println(part2(input))
}