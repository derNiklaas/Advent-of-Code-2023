import utils.splitAndMapToInt

class Day04 : AoCDay("04") {

    private val parsedInput = input.map(ScratchTicket::fromInput)

    private val winningTickets = mutableMapOf<Int, Long>()

    override fun part1(): Any {
        return parsedInput.sumOf {
            val winning = it.winningNumbers
            val numbers = it.numbers
            var points = 0
            for (number in numbers) {
                if (number in winning) {
                    if (points == 0) points = 1
                    else points *= 2
                }
            }
            points
        }
    }

    override fun part2(): Any {
        var tickets = parsedInput.size.toLong()
        for (ticket in parsedInput) {
            tickets += getTotalTickets(ticket.id)
        }

        return tickets
    }


    private fun getTotalTickets(id: Int): Long {
        val ticket = parsedInput.find { it.id == id } ?: return 0
        val winningNumbers = ticket.winningNumbers
        val numbers = ticket.numbers
        val correctMatches = numbers.filter { it in winningNumbers }.size
        if (correctMatches == 0) {
            winningTickets[ticket.id] = 0
        } else {
            var total = 0L
            for (i in 1..correctMatches) {
                val copiedTicket = parsedInput.find { it.id == id + i } ?: continue
                val totalTickets = getTotalTickets(copiedTicket.id)
                total += totalTickets + 1
            }
            winningTickets[ticket.id] = total
        }
        return winningTickets[ticket.id]!!
    }

}

private data class ScratchTicket(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
    companion object {
        fun fromInput(input: String): ScratchTicket {
            val (id, code) = input.removePrefix("Card").split(": ")
            val (winning, numbers) = code.split("|").splitAndMapToInt()
            return ScratchTicket(id.trim().toInt(), winning, numbers)
        }
    }
}

fun main() {
    Day04().execute()
}