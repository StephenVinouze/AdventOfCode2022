fun main() {

    fun part1(input: List<String>): Int {
        val numbers = input.mapIndexed { index, value -> Pair(value.toLong(), index) }
        var arrangedNumbers = numbers.toMutableList()
        val size = numbers.size
        numbers.forEach { number ->
            val indexOfNumber = arrangedNumbers.indexOf(number)
            //newList.add()
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    //check(part1(readInput("Day20_test")) == 3)
    //check(part2(readInput("Day20_test")) == 58)

    val input = readInput("Day20")
    println(part1(input))
    //println(part2(input))
}