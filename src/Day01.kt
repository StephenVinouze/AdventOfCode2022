fun main() {
    fun caloriesPerEfl(input: List<String>): List<Int> {
        var caloriesPerElf = 0
        val elfCalories = mutableListOf<Int>()
        input.forEachIndexed { index, calories ->
            if (calories.isBlank() || index == input.lastIndex) {
                elfCalories.add(caloriesPerElf)
                caloriesPerElf = 0
            } else {
                caloriesPerElf += calories.toInt()
            }
        }
        return elfCalories
    }

    fun part1(input: List<String>): Int =
        caloriesPerEfl(input).max()

    fun part2(input: List<String>): Int =
        caloriesPerEfl(input).sortedDescending().take(3).sum()

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
