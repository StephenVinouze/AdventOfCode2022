data class Directory(
    val name: String,
    val head: String?,
    val directories: MutableList<Directory> = mutableListOf(),
    val files: MutableList<File> = mutableListOf(),
)

data class File(
    val name: String,
    val size: Int,
)

fun main() {

    fun computeDirectorySize(directory: Directory): Int {
        val subdirectorySizes = directory.directories.sumOf { computeDirectorySize(it) }
        val fileSizes = directory.files.sumOf { it.size }
        return subdirectorySizes + fileSizes
    }

    fun List<String>.toDirectorySizes(): List<Int> {
        val directories = mutableListOf<Directory>()
        var currentDirectory = Directory("/", null)
        directories.add(currentDirectory)
        this
            .drop(1)
            .filter { it != "$ ls" }
            .forEach {
                val splitInput = it.split(" ")
                when {
                    it.startsWith("$ cd") -> {
                        currentDirectory = when (val directoryName = splitInput.last()) {
                            ".." -> directories.first { directory -> directory.name == currentDirectory.head && currentDirectory in directory.directories }
                            else -> currentDirectory.directories.first { directory -> directory.name == directoryName }
                        }
                    }

                    it.startsWith("dir") -> {
                        val newDirectory = Directory(splitInput.last(), currentDirectory.name)
                        directories.add(newDirectory)
                        currentDirectory.directories.add(newDirectory)
                    }

                    else -> currentDirectory.files.add(File(splitInput.last(), splitInput.first().toInt()))
                }
            }

        return directories.map { computeDirectorySize(it) }
    }

    fun part1(input: List<String>): Int =
        input
            .toDirectorySizes()
            .filter { it < 100000 }
            .sum()

    fun part2(input: List<String>): Int {
        val directorySizes = input.toDirectorySizes()
        val maxSpace = 70000000
        val updateSpace = 30000000
        val unusedSpace = maxSpace - directorySizes.first()
        val wantedSpace = updateSpace - unusedSpace
        return directorySizes
            .sorted()
            .first { it > wantedSpace }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}