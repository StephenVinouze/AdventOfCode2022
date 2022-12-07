fun main() {

    fun itemPriority(item: Char): Int {
        // char ascii code starts at 65 for uppercase letters and 97 for lowercases
        // Shifting uppercases for additional 26 to count after lowercases letters
        val offset = if (Character.isUpperCase(item)) 65 - 26 else 97
        return item.code - offset + 1
    }

    fun part1(input: List<String>): Int =
        input
            .map {
                val items = it.toCharArray().toList()
                val compartments = items.chunked(items.size / 2)
                val firstCompartment = compartments.first()
                val secondCompartment = compartments.last()
                firstCompartment.intersect(secondCompartment.toSet())
            }
            .sumOf { identicalItems ->
                identicalItems.sumOf { item ->
                    itemPriority(item)
                }
            }

    fun part2(input: List<String>): Int =
        input
            .chunked(3)
            .map { it.map { rucksack -> rucksack.toCharArray().toList() } }
            .sortedByDescending { it.size }
            .sumOf { groupRucksack ->
                val biggerRucksack = groupRucksack[0]
                val mediumRucksack = groupRucksack[1]
                val smallerRucksack = groupRucksack[2]
                var groupItem: Char? = null
                for (i in 0..biggerRucksack.lastIndex) {
                    val item = biggerRucksack[i]
                    if (mediumRucksack.contains(item) && smallerRucksack.contains(item)) {
                        groupItem = item
                        break
                    }
                }

                if (groupItem == null) throw IllegalStateException("Must have common item")

                itemPriority(groupItem)
            }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}