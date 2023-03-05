import java.io.File

fun main(args: Array<String>) {

    val crtW = 40
    val crtH = 6

    data class Instruction(val ticks: Int = 1, val inc: Int = 0)

    fun crtDisplay(input: List<String>) {
        val instructions: List<Instruction> = buildList {
            // transform a list of Strings to a list of Instructions
            for (s in input) {
                // transform String to Instruction
                val opcode = s.substringBefore(" ")
                val instruction = when (opcode) {
                    "noop" -> Instruction()
                    "addx" -> Instruction(ticks = 2, inc = s.substringAfter(" ").toInt())
                    else -> error("oops")
                }
                this.add(instruction)
            }
        }
        val xRegisterAtTick: List<Int> = buildList {
            // transform instructions to x register values
            var x = 1 // needs a running x register value
            for (i in instructions) {
                repeat(i.ticks) {
                    this.add(x)
                }
                x += i.inc
            }
            this.add(x)
        }
        for (i in 0 until (crtW * crtH)) {
            // transform xRegister values to pixels(String)
            // chop up in lines
            // side effect: print pixels
            if (i.mod(crtW) == 0) print("\n")                                                      //<============  side effect
            print(if (i.mod(crtW) in xRegisterAtTick[i] - 1..xRegisterAtTick[i] + 1) "█" else ".") //<============  side effect
        }
    }

    val testInput = readInput("Day_test")
    println(" ============== test input =============")
    crtDisplay(testInput)
    println("\n\n")
    println("============== real input ==============")
    val input = readInput("Day")
    crtDisplay(input)
    println("\n\n")

}

fun readInput(name: String) = File("src/main/resources/", "$name.txt").readLines()