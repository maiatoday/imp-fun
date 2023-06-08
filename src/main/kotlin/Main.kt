import java.io.File

/**
 * The main function implements a CRT scanning and display algorithm to convert a list of input strings with instructions into a visual representation.
 * The program contains several inner functions and classes to handle instructions, perform the scanning, and display the resulting pixel data.
 *
 * @param args Unused command-line arguments.
 */
fun main(args: Array<String>) {

    val crtW = 40

    data class Instruction(val ticks: Int = 1, val inc: Int = 0)

    /**
     * Converts a string to an Instruction object according to the string prefix.
     *
     * - "noop" prefix results in a default Instruction object with ticks = 1 and inc = 0.
     * - "addx" prefix results in an Instruction object with ticks = 2 and inc as the integer value following the prefix.
     * - Any other prefix throws an error.
     *
     * @return The Instruction object resulting from the conversion.
     * @throws IllegalArgumentException If the string prefix is not "noop" or "addx".
     */
    fun String.toInstruction() = when (substringBefore(" ")) {
        "noop" -> Instruction()
        "addx" -> Instruction(ticks = 2, inc = substringAfter(" ").toInt())
        else -> error("oops")
    }

    fun Instruction.expandInstruction(): List<Int> =
        buildList {
            repeat(this@expandInstruction.ticks - 1) { this.add(0) }
            this.add(this@expandInstruction.inc)
        }

    fun Int.toPixel(index: Int): String = if (index % crtW in this - 1..this + 1) "🔴" else "⚫️"

    /**
     * Converts a list of input strings with instructions into a visual representation by processing each instruction, calculating accumulated
     * X-register value, and generating pixels for output.
     *
     * 1. Converts input strings to Instruction objects using 'toInstruction' function.
     * 2. Expands multi-tick instructions using 'expandInstruction' function.
     * 3. Creates a list with accumulated X-register values using 'runningFold' function.
     * 4. Converts the index and X-register value to a pixel using 'toPixel' function.
     * 5. Chunks the list into lines of 40 pixels and concatenates them using 'joinToString'.
     *
     * @param input List of strings representing instructions to process.
     * @return A list of strings representing visual representation of instructions.
     */
    fun crtScan(input: List<String>, crtW: Int = 40): List<String> =
        input.map { it.toInstruction() }// converts input to instruction
            .flatMap { i -> i.expandInstruction() } // expands multi tick instructions
            .runningFold(1) { x, i -> x + i } // runs through the instructions accumulating x
            .mapIndexed { index, x -> x.toPixel(index) } // converts index and x register to a pixel
            .chunked(crtW).map { it.joinToString("") } // spilt into lines for the screen


    fun List<String>.display() { // side effect method
        this.forEach(::println)
    }

    val testInput = readInput("Day_test")
    println(" ============== test input =============")
    crtScan(testInput).display()
    println("============== real input ==============")
    val input = readInput("Day")
    crtScan(input).display()
    println("\n\n")
}

fun readInput(name: String) = File("src/main/resources/", "$name.txt").readLines()
