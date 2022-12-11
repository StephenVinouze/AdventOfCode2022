import java.lang.IllegalArgumentException

data class Instruction(
    val cyclesCount: Int,
    val value: Int,
)

fun main() {

    fun List<String>.toInstructions(): List<Instruction> =
        map {
            when {
                it.startsWith("noop") -> Instruction(1, 0)
                it.startsWith("addx") -> Instruction(2, it.split(" ").last().toInt())
                else -> throw IllegalArgumentException("Unknown instruction name : $it")
            }
        }

    fun computeCycles(input: List<String>): List<Int> {
        var currentCycleRegister = 1
        val cycles = mutableListOf(currentCycleRegister)
        input.toInstructions().forEach {
            when (it.cyclesCount) {
                1 -> cycles.add(currentCycleRegister)
                2 -> {
                    val cycleRegister = currentCycleRegister + it.value
                    cycles.add(currentCycleRegister)
                    cycles.add(cycleRegister)
                    currentCycleRegister = cycleRegister
                }
            }
        }
        return cycles
    }

    fun part1(input: List<String>): Int =
        computeCycles(input)
            .mapIndexedNotNull { index, i ->
                if ((index + 1) in listOf(20, 60, 100, 140, 180, 220))
                    i * (index + 1)
                else
                    null
            }
            .sum()

    fun part2(input: List<String>): String {
        var crt = ""
        var spritePosition = 0
        computeCycles(input)
            .drop(1)
            .forEachIndexed { index, cycle ->
                crt += if (index % 40 in (spritePosition..spritePosition + 2)) "#" else "."
                if ((index + 1) in listOf(40, 80, 120, 160, 200, 240)) crt += "\n"
                spritePosition = cycle - 1
            }
        return crt
    }

    check(part1(readInput("Day10_test")) == 13140)
    println(part2(readInput("Day10_test")))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}