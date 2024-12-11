import kotlin.math.abs

fun List<Int>.allIncreaseByOneOrTwo(fudgeFactor: Int = 0): Int {
    var direction = ""
    // Need to figure out how to skip a level
    var timesSkipped = 0
    var shouldSkip = false

    forEachIndexed { index, item ->
        if (shouldSkip) {
            shouldSkip = false;
            return@forEachIndexed
        }
        // if at the end then we made it and can return true
        // because we already checked if going up or down by 1 was valid on the previous iteration
        if (index == size - 1) return 1
        // if at any point we've skipped more than one number then we aren't safe
        if (timesSkipped > fudgeFactor) return 0

        val nextItem = this[index + 1]
        val nextNextItem = if (this.size > index + 2) this[index + 2] else null
//        println("-".repeat(50))

//        item.println()
//        nextItem.println()
//        nextNextItem.println()
        val isGoingUpOneInvalid = nextItem - item > 3 || nextItem - item < 1
        val isGoingDownOneInvalid = item - nextItem > 3 || item - nextItem < 1
        val isGoingUpTwoInvalid = nextNextItem != null && (nextNextItem - item > 3 || nextNextItem - item < 1)
        val isGoingDownTwoInvalid = nextNextItem != null && (item - nextNextItem > 3 || item - nextNextItem < 1)
//        println("isGoingUpOneInvalid: $isGoingUpOneInvalid")
//        println("isGoingDownOneInvalid: $isGoingDownOneInvalid")
//        println("isGoingUpTwoInvalid: $isGoingUpTwoInvalid")
//        println("isGoingDownTwoInvalid: $isGoingDownTwoInvalid")
        // set the initial direction
        if (direction.isEmpty()) {
            if (isGoingUpOneInvalid.not() || isGoingUpTwoInvalid.not()) {
                direction = "up"
            } else if (isGoingDownOneInvalid.not() || isGoingDownTwoInvalid.not()) {
                direction = "down"
            }
        }
        direction.println()
//        println("-".repeat(50))
        when (direction) {
            // if going up and the next one is up by 1 or 2 then keep going
            "up" -> {
                if (isGoingUpOneInvalid && timesSkipped >= fudgeFactor) {
                    return 0
                } else if (isGoingUpOneInvalid && isGoingUpTwoInvalid) {
                    // if this step up is invalid and the next step up is invalid then
                    // it doesn't matter which one we remove
                    return 0
                } else if (isGoingUpOneInvalid) {
                    // at this point we need to know to skip the next index because it was invalid
                    // but 2 indexes were valid
                    timesSkipped += 1
                    shouldSkip = true
                }
            }
            // if going down and the next one is down by 1 or 2 then keep going
            "down" -> {
                if (isGoingDownOneInvalid && timesSkipped >= fudgeFactor) {
                    return 0
                } else if (isGoingDownOneInvalid && isGoingDownTwoInvalid) {
                    return 0
                } else if (isGoingDownOneInvalid) {
                    timesSkipped += 1
                    shouldSkip = true
                }
            }
            // otherwise return 0 because it isn't increasing or decreasing at the correct rate
            else -> if (index == 0) {
                println("should skip because index is 0 and there were no valid directions")
                println("the value $item")
                timesSkipped += 1
            } else {
                return 0
            }
        }

    }

    return 1
}

fun List<Int>.allAreGoingUp() = windowed(2).fold(true) { result, (left, right) ->
    // if we already hit a false then we keep returning false the whole way
    kotlin.io.println("left: $left, right: $right, abs: ${abs(left - right)}")
    result && left - right in 1..3
}

fun List<Int>.allAreGoingDown() = windowed(2).fold(true) { result, (left, right) ->
    // if we already hit a false then we keep returning false the whole way
    result && right - left in 1..3
}

fun List<Int>.isValid() = if (allAreGoingUp() || allAreGoingDown()) 1 else 0

fun List<Int>.isValidWithoutOne(): Int {
    val hasOneValidPath = indices.any { index ->
        omit(index).isValid() == 1
    }

    return if (hasOneValidPath) 1 else 0
}

fun List<Int>.omit(index: Int) = subList(0, index) + subList(index + 1, size)

fun main() {
    fun part1(input: List<String>): Int {
//        input.println()
        return input.map { line ->
            line.split(" ").map { it.toInt() }
        }.sumOf { line ->
            println("Input: $line")
            val output = line.isValid()
            println("Output: $output")
            output
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            line.split(" ").map { it.toInt() }
        }.sumOf { line ->
            val alreadyOrdered = line.isValid()

            if (alreadyOrdered == 1) alreadyOrdered else {
                line.isValidWithoutOne()
            }
        }
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    part1(testInput).println()
    check(part1(testInput) == 3)
    check(part2(testInput) == 12)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
