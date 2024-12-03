import kotlin.math.abs

fun List<Int>.allIncreaseByOneOrTwo(fudgeFactor: Int = 0): Int {
    var direction = ""
    // Need to figure out how to skip a level

    forEachIndexed { index, item ->
        // if at the end then we made it and can return true
        if (index == size - 1) return 1

        val nextItem = this[index + 1]

        // set the initial direction
        if (direction.isEmpty()) {
            if (item < nextItem) {
                direction = "up"
            } else if (item > nextItem) {
                direction = "down"
            }
        }

        when (direction) {
        // if going up and the next one is up by 1 or 2 then keep going
            "up" -> {
                if (nextItem - item > 3 || nextItem - item < 1) return 0
            }
        // if going down and the next one is down by 1 or 2 then keep going
            "down" -> {
                if (item - nextItem > 3 || item - nextItem < 1) return 0
            }
        // otherwise return 0 because it isn't increasing or decreasing at the correct rate
            else -> return 0
        }

    }

    return 1
}

fun main() {
    fun part1(input: List<String>): Int {
        input.println()
        return input.map { line ->
            line.split(" ").map { it.toInt() }
        }.sumOf { line ->
            line.allIncreaseByOneOrTwo()
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
