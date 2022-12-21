data class YellingMonkey(
    val name: String,
    val number: Long?,
    val operator: Operator?,
    val waitingForMonkeys: List<String>,
)

enum class Operator(val value: Char) {
    Add('+'),
    Substract('-'),
    Multiply('*'),
    Divide('/'),
}

fun main() {

    fun List<String>.toYellingMonkeys(): List<YellingMonkey> =
        map { line ->
            val name = line.substringBefore(":")
            val operation = line.substringAfter(":").trim()
            val isYellingNumber = operation.first().isDigit()
            val number = if (isYellingNumber) operation.toLong() else null
            val (operator, waitingForMonkeys) = operation.split(" ").let {
                val operator = if (isYellingNumber) null else Operator.values().first { operator -> operator.value == it[1].first() }
                val waitingForMonkeys = if (isYellingNumber) emptyList() else listOf(it[0], it[2])
                operator to waitingForMonkeys
            }
            YellingMonkey(
                name = name,
                number = number,
                operator = operator,
                waitingForMonkeys = waitingForMonkeys
            )
        }

    fun part1(input: List<String>): Long {
        val monkeys = input.toYellingMonkeys().toMutableList()

        while (monkeys.first { it.name == "root" }.number == null) {
            monkeys.forEachIndexed { index, monkey ->
                if (monkey.waitingForMonkeys.isNotEmpty()) {
                    val waitingForMonkeys = monkey.waitingForMonkeys.map { name -> monkeys.first { it.name == name } }
                    val firstMonkeyNumber = waitingForMonkeys.first().number
                    val secondMonkeyNumber = waitingForMonkeys.last().number
                    val updatedMonkey = if (firstMonkeyNumber != null && secondMonkeyNumber != null) {
                        val number = when (monkey.operator) {
                            Operator.Add -> firstMonkeyNumber + secondMonkeyNumber
                            Operator.Substract -> firstMonkeyNumber - secondMonkeyNumber
                            Operator.Multiply -> firstMonkeyNumber * secondMonkeyNumber
                            Operator.Divide -> firstMonkeyNumber / secondMonkeyNumber
                            null -> throw IllegalStateException("Monkey should have an operator at this point!")
                        }
                        monkey.copy(number = number, waitingForMonkeys = emptyList())
                    } else monkey
                    monkeys[index] = updatedMonkey
                }
            }
        }

        return monkeys.first { it.name == "root" }.number!!
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.toYellingMonkeys().toMutableList()
        return 0L
    }

    check(part1(readInput("Day21_test")) == 152L)
    //check(part2(readInput("Day21_test")) == 301L)

    val input = readInput("Day21")
    println(part1(input))
    //println(part2(input))
}