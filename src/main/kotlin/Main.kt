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

    fun buildXRegisterAtTick(instructions: List<Instruction>): MutableList<Int> {
        val xRegisterAtTick = mutableListOf<Int>()
        var x = 1 // needs a running x register value
        for (instruction in instructions) {
            for (i in 0 until instruction.ticks) {
                xRegisterAtTick.add(x)
            }
            x += instruction.inc
        }
        xRegisterAtTick.add(x)
        return xRegisterAtTick
    }

    fun printCRT(input: List<String>) {
        val instructions: MutableList<Instruction> = mutableListOf()
        for (s in input) {
            instructions.add(parseInstruction(s))
        }
        val xRegisterAtTick = buildXRegisterAtTick(instructions)

        for (i in 0 until (crtW * crtH)) {
            if (i % crtW == 0) print("\n")
            if (i % crtW in xRegisterAtTick[i] - 1..xRegisterAtTick[i] + 1) {
                print("🔴") 
            } else {
                print("⚫️")
            }
        }
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