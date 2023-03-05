import java.io.File

fun main(args: Array<String>) {

    val crtW = 40

    data class Instruction(val ticks: Int = 1, val inc: Int = 0)

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

    fun crtScan(input: List<String>): List<String> =
        input.map { it.toInstruction() }// converts input to instruction
            .flatMap { i -> i.expandInstruction() } // expands multi tick instructions
            .runningFold(1) { x, i -> x + i } // runs through the instructions accumulating x
            .mapIndexed { index, x -> x.toPixel(index) } // converts index and x register to a pixel
            .chunked(40).map { it.joinToString("") } // spilt into lines for the screen


    fun List<String>.display() { // side effect method
        this.forEach {
            println(it)
        }
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
