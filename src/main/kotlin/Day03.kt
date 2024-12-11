fun MatchResult.sumMatch(): Int {
    val left = groupValues[1].toIntOrNull()
    val right = groupValues[2].toIntOrNull()

    return if (left != null && right != null) left * right else 0
}

fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("""mul\((\d+),(\d+)\)""")

        return input.sumOf { line ->
            // split each line into sections
            val matches = regex.findAll(line)

            matches.sumOf { it.sumMatch() }
        }
    }

    fun part2(input: List<String>): Int {
        val regex = Regex("""mul\((\d+),(\d+)\)""")
        var shouldCalculate = true

        return input.sumOf { line ->
            var calcStartIndex = 0
            var lineSum = 0

            while (calcStartIndex < line.length) {
                // go until I hit either do() if shouldCalculate is false
                // or don't() if shouldCalculate is true
                // sum everything prior to that point if shouldCalculate was true before
                // then flip the shouldCalculate switch
                // then continue with the next section
                val nextDoNotIndex = line.indexOf("don't()", startIndex = calcStartIndex)
                val nextDoIndex = line.indexOf("do()", startIndex = calcStartIndex)

                // if there are no more do() sections then we won't need to calculate anything else
                if (nextDoIndex == -1 && shouldCalculate.not()) {
                    break
                    // if there are no more don't() sections and we should calculate then calculate the rest
                } else if (nextDoNotIndex == -1 && shouldCalculate) {
                    val sectionToSum = line.substring(calcStartIndex, line.length)
                    val matches = regex.findAll(sectionToSum)

                    lineSum += matches.sumOf { it.sumMatch() }

                    break
                }

                if (shouldCalculate) {
                    // if we should calculate then check if there is another dont()
                    // if there is then we calculate from here until there
                    // if there isn't then we calculate from here to the end of the line
                    val sectionToSum = line.substring(calcStartIndex, nextDoNotIndex)
                    val matches = regex.findAll(sectionToSum)

                    lineSum += matches.sumOf { it.sumMatch() }

                    shouldCalculate = false
                    calcStartIndex = nextDoNotIndex + 7
                } else {
                    // if there is another do() and we had previously hit a dont() then we want to flip the switch
                    // and calculate from here
                    if (nextDoIndex != -1) {
                        shouldCalculate = true
                        calcStartIndex = nextDoIndex + 4
                    }
                }
            }

            lineSum
        }
    }

    "don't()".indexOf("don't()").println()

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInput("Day03_test")
    part1(testInput).println()
    part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))\n")).println()
    check(part1(testInput) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))\n")) == 48)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
