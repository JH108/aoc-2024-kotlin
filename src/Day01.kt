import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val leftHandList = input.map { it.substringBefore(" ").toInt() }.sorted()
        val rightHandList = input.map { it.substringAfter(" ").trim().toInt() }.sorted()

        return leftHandList.zip(rightHandList).sumOf { (left, right) -> abs(left - right) }
    }

    fun part2(input: List<String>): Int {
        val leftHandList = input.map { it.substringBefore(" ").toInt() }
        val rightHandList = input.map { it.substringAfter(" ").trim().toInt() }.fold(mapOf<Int, Int>()) { counts, num ->
            counts + (num to counts.getOrDefault(num, 0) + 1)
        }

        return leftHandList.sumOf { num ->
            num * rightHandList.getOrDefault(num, 0)
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
