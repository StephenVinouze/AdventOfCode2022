fun main() {

    fun <T> List<List<T>>.rowToColumn(): List<List<T>> =
        first().indices
            .map { row -> indices.map { col -> this[col][row] } }

    fun List<List<Char>>.toTopStack(): String =
        map { it.last() }.joinToString("")

    fun parse(input: List<String>): Pair<List<MutableList<Char>>, List<List<String>>> {
        val (unformattedStacks, unformattedInstructions) = input.partition { it.contains('[') }
        val stackSize = unformattedInstructions.first()
            .last { it.isDigit() }
            .toString()
            .toInt()
        val stacks = unformattedStacks
            .dropLastWhile { !it.contains('[') }
            .map {
                // Drop first whitespace then take only stack char
                val stack = it.toCharArray().toList()
                    .drop(1)
                    .filterIndexed { index, _ -> index % 4 == 0 }
                    .toMutableList()

                // Fill shorter stack lines with whitespaces so that we can flip the rows into columns
                // (we need each row to have same size)
                repeat((stackSize - stack.size)) {
                    stack.add(' ')
                }
                stack
            }
            .rowToColumn()
            .map {
                // Reverse stacks to have top stack at the end of the list then remove useless whitespaces
                it.reversed()
                    .filterNot { stack -> stack.isWhitespace() }
                    .toMutableList()
            }

        val instructions = unformattedInstructions
            .drop(2)
            .map { instruction -> instruction
                .filter { it.isDigit() || it.isWhitespace() }
                .split(" ")
                .filter { it.isNotBlank() }
            }
        return Pair(stacks, instructions)
    }

    fun part1(input: List<String>): String {
        val (stacks, instructions) = parse(input)

        instructions.forEach { instruction ->
            val stacksToMove = instruction[0].toInt()
            val fromStack = stacks[instruction[1].toInt() - 1]
            val toStack = stacks[instruction[2].toInt() - 1]

            val movedItems = mutableListOf<Char>()
            val movingStacksCount = stacksToMove.coerceIn(0, fromStack.size)
            repeat(movingStacksCount) {
                val movedItem = fromStack[fromStack.lastIndex - it]
                toStack.add(movedItem)
                movedItems.add(movedItem)
            }
            repeat(movingStacksCount) {
                fromStack.removeLast()
            }
        }

        return stacks.toTopStack()
    }

    fun part2(input: List<String>): String {
        val (stacks, instructions) = parse(input)

        instructions.forEach { instruction ->
            val stacksToMove = instruction[0].toInt()
            val fromStack = stacks[instruction[1].toInt() - 1]
            val toStack = stacks[instruction[2].toInt() - 1]

            val movingStacksCount = stacksToMove.coerceIn(0, fromStack.size)
            val movingStacks = fromStack.slice((fromStack.size - movingStacksCount).. fromStack.lastIndex)

            toStack.addAll(movingStacks)
            repeat(movingStacksCount) {
                fromStack.removeLast()
            }
        }

        return stacks.toTopStack()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}