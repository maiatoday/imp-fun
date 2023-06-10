import java.io.File

fun main(args: Array<String>) {
    val crtW = 40
    val crtH = 6

    data class Instruction(val ticks: Int = 1, val inc: Int = 0)

    fun parseInstruction(s: String): Instruction {
        val opcode = s.substringBefore(" ")
        return when (opcode) {
            "noop" -> Instruction()
            "addx" -> Instruction(ticks = 2, inc = s.substringAfter(" ").toInt())
            else -> error("oops")
        }
    }

    fun buildXRegisterAtTick(instructions: List<Instruction>): List<Int> {
        var x = 1 // needs a running x register value
        return instructions.flatMap { instruction ->
            List(instruction.ticks) { x }.apply { x += instruction.inc }
        } + x
    }

    fun printCRT(input: List<String>) {
        val instructions = input.map(::parseInstruction)
        val xRegisterAtTick = buildXRegisterAtTick(instructions)

        val crtRows = (crtW * crtH until 0 step -crtW).reversed().map { lineStart ->
            (lineStart until lineStart + crtW).joinToString("") { i ->
                if (i % crtW in xRegisterAtTick[i] - 1..xRegisterAtTick[i] + 1) "█" else "."
            }
        }

        println(crtRows.joinToString("\n"))
    }

    val testInput = readInput("Day_test")
    println(" ============== test input =============")
    printCRT(testInput)
    println("\n\n")
    println("============== real input ==============")
    val input = readInput("Day")
    printCRT(input)
    println("\n\n")
}

fun readInput(name: String) = File("src/main/resources/", "$name.txt").readLines()