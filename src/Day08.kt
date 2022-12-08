data class OuterTrees(
    val topTrees: List<Int>,
    val leftTrees: List<Int>,
    val rightTrees: List<Int>,
    val bottomTrees: List<Int>,
)

fun main() {

    fun List<String>.toTrees(): List<List<Int>> =
        map {
            it.toCharArray().toList().map { treeChar ->
                treeChar.digitToInt()
            }
        }

    fun isTreeVisible(tree: Int, outerTrees: OuterTrees): Boolean =
        listOf(outerTrees.topTrees, outerTrees.leftTrees, outerTrees.rightTrees, outerTrees.bottomTrees)
            .any { sideTrees ->
                sideTrees.isEmpty() || sideTrees.all { it < tree }
            }

    fun computeScenicScore(tree: Int, outerTrees: OuterTrees): Int =
        listOf(outerTrees.topTrees, outerTrees.leftTrees, outerTrees.rightTrees, outerTrees.bottomTrees)
            .map { sideTrees ->
                val visibleTrees = sideTrees.takeWhile { it < tree }.size
                if (visibleTrees != 0 && visibleTrees < sideTrees.size) visibleTrees + 1 else visibleTrees
            }
            .fold(1) { total, scenicScore ->
                total * scenicScore
            }

    fun outerTrees(
        trees: List<List<Int>>,
        treeRow: List<Int>,
        treeRowIndex: Int,
        treeIndex: Int,
    ): OuterTrees {
        val topTrees = mutableListOf<Int>()
        val leftTrees = mutableListOf<Int>()
        val rightTrees = mutableListOf<Int>()
        val bottomTrees = mutableListOf<Int>()

        (0 until treeRow.lastIndex).forEachIndexed { index, _ ->
            trees.getOrNull(treeRowIndex - 1 - index)?.getOrNull(treeIndex)?.let { topTrees += it }
            trees.getOrNull(treeRowIndex)?.getOrNull(treeIndex - 1 - index)?.let { leftTrees += it }
            trees.getOrNull(treeRowIndex)?.getOrNull(treeIndex + 1 + index)?.let { rightTrees += it }
            trees.getOrNull(treeRowIndex + 1 + index)?.getOrNull(treeIndex)?.let { bottomTrees += it }
        }

        return OuterTrees(topTrees, leftTrees, rightTrees, bottomTrees)
    }

    fun part1(input: List<String>): Int {
        var scenicScore = 0
        val trees = input.toTrees()

        trees.forEachIndexed { treeRowIndex, treeRow ->
            treeRow.forEachIndexed { treeIndex, tree ->
                val outerTrees = outerTrees(trees, treeRow, treeRowIndex, treeIndex)
                if (isTreeVisible(tree, outerTrees)) {
                    scenicScore++
                }
            }
        }

        return scenicScore
    }

    fun part2(input: List<String>): Int {
        var maxScenicScore = 0
        val trees = input.toTrees()

        trees.forEachIndexed { treeRowIndex, treeRow ->
            treeRow.forEachIndexed { treeIndex, tree ->
                val outerTrees = outerTrees(trees, treeRow, treeRowIndex, treeIndex)
                val scenicScore = computeScenicScore(tree, outerTrees)
                if (scenicScore > maxScenicScore) {
                    maxScenicScore = scenicScore
                }
            }
        }
        return maxScenicScore
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}