fun main() {
    fun caloriesPerEfl(input: List<String>): List<Int> {
        var caloriesPerElf = 0
        val elfCalories = mutableListOf<Int>()
        input.forEachIndexed { index, calories ->
            when {
                calories.isBlank() -> {
                    elfCalories.add(caloriesPerElf)
                    caloriesPerElf = 0
                }
                else -> {
                    caloriesPerElf += calories.toInt()
                    if (index == input.lastIndex && input[input.lastIndex - 1].isBlank()) {
                        elfCalories.add(caloriesPerElf)
                    }
                }
            }
        }
        return elfCalories
    }

    fun part1(input: List<String>): Int =
        caloriesPerEfl(input).max()

    fun part2(input: List<String>): Int =
        caloriesPerEfl(input).sortedDescending()
            .take(3)
            .sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
