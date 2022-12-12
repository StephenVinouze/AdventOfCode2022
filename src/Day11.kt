import java.lang.IllegalArgumentException
import kotlin.math.floor

data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val throwToMonkey: (Long) -> Unit,
    val divider: Int,
)

fun main() {

    fun List<String>.toMonkeys(): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        this
            .filter { it.isNotBlank() }
            .chunked(6)
            .forEach { line ->
                monkeys.add(
                    Monkey(
                        id = line[0].drop("Monkey ".length)
                            .first()
                            .digitToInt(),
                        items = line[1]
                            .drop("  Starting items: ".length)
                            .filterNot { it.isWhitespace() }
                            .split(",")
                            .map { it.toLong() }
                            .toMutableList(),
                        operation = { worryLevel ->
                            val (operatorStr, valueStr) = line[2]
                                .drop("  Operation: new = old ".length)
                                .split(" ")
                            val value = when (valueStr) {
                                "old" -> worryLevel
                                else -> valueStr.toLong()
                            }
                            when (operatorStr) {
                                "+" -> worryLevel + value
                                "*" -> worryLevel * value
                                "-" -> worryLevel - value
                                "/" -> worryLevel / value
                                else -> throw IllegalArgumentException("Unknown operator : $operatorStr")
                            }
                        },
                        throwToMonkey = { worryLevel ->
                            val divider = line[3]
                                .drop("  Test: divisible by ".length)
                                .toInt()
                            val monkeyIdIfTrue = line[4]
                                .drop("    If true: throw to monkey ".length)
                                .toInt()
                            val monkeyIdIfFalse = line[5]
                                .drop("    If false: throw to monkey ".length)
                                .toInt()
                            val monkeyId = if (worryLevel % divider == 0L) monkeyIdIfTrue else monkeyIdIfFalse
                            monkeys.first { it.id == monkeyId }.items.add(worryLevel)
                        },
                        divider = line[3]
                            .drop("  Test: divisible by ".length)
                            .toInt(),
                    )
                )
            }
        return monkeys
    }

    fun compute(monkeys: List<Monkey>, rounds: Int, keepWorryLevelManageable: (Long) -> Long): Long {
        val inspectedItemsPerMonkey = MutableList(monkeys.size) { 0 }
        repeat(rounds) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { inspectedItem ->
                    val worryLevel = keepWorryLevelManageable(monkey.operation(inspectedItem))
                    monkey.throwToMonkey(worryLevel)
                }
                inspectedItemsPerMonkey[monkey.id] = inspectedItemsPerMonkey[monkey.id] + monkey.items.size
                monkey.items.clear()
            }
        }
        return inspectedItemsPerMonkey
            .sortedDescending()
            .take(2)
            .fold(1) { acc, i -> acc * i }
    }

    fun part1(input: List<String>): Long {
        val monkeys = input.toMonkeys()
        return compute(monkeys, 20) { worryLevel ->
            floor((worryLevel / 3.0)).toLong()
        }
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.toMonkeys()
        val lcm = monkeys.fold(1) { acc, monkey -> acc * monkey.divider }
        return compute(monkeys, 10000) { worryLevel ->
            worryLevel % lcm
        }
    }

    check(part1(readInput("Day11_test")) == 10605L)
    check(part2(readInput("Day11_test")) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}