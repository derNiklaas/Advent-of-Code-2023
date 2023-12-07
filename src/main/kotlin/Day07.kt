import utils.AoCDay

class Day07 : AoCDay("07") {

    companion object {
        var partB = false
            private set
    }

    private val rounds: List<CardRound> = input.map { line ->
        val (cards, winnings) = line.split(" ")
        val occurrences = mutableMapOf<Char, Int>()
        cards.forEach { card ->
            occurrences[card] = occurrences.getOrDefault(card, 0) + 1
        }
        CardRound(cards, occurrences, winnings.toInt())
    }

    override fun part1(): Any {
        val sortedRounds = rounds.sorted()
        var sum = 0L
        sortedRounds.forEachIndexed { index, round ->
            sum += (index + 1) * round.winnings
        }
        return sum
    }

    override fun part2(): Any {
        partB = true
        val sortedRounds = rounds.map { round ->
            val newCards = round.useJoker()
            CardRound(round.originalString, newCards, round.winnings)
        }.sorted()
        var sum = 0L
        sortedRounds.forEachIndexed { index, round ->
            sum += (index + 1) * round.winnings
        }
        return sum
    }
}

data class CardRound(val originalString: String, val cards: Map<Char, Int>, val winnings: Int) : Comparable<CardRound> {

    companion object {
        val cardValues = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 11,
            'T' to 10,
            '9' to 9,
            '8' to 8,
            '7' to 7,
            '6' to 6,
            '5' to 5,
            '4' to 4,
            '3' to 3,
            '2' to 2
        )

        val cardValuesPartB = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'T' to 10,
            '9' to 9,
            '8' to 8,
            '7' to 7,
            '6' to 6,
            '5' to 5,
            '4' to 4,
            '3' to 3,
            '2' to 2,
            'J' to 1,
        )
    }

    /**
     * Compares this object with the specified object for order.
     * Returns zero if this object is equal to the specified other object, a negative number if it's less than other, or a positive number if it's greater than [other].
     */
    override fun compareTo(other: CardRound): Int {
        if (hasAllCards()) {
            if (other.hasAllCards()) {
                for (i in 0..4) {
                    val thisValue = getCardValue(originalString[i])
                    val otherValue = getCardValue(other.originalString[i])
                    if (thisValue != otherValue) {
                        return thisValue.compareTo(otherValue)
                    }
                }
            }
            return 1
        }

        // The other player has more cards, so they win
        if (other.hasAllCards()) return -1

        /*
        FOUR CARDS + ONE CARD
         */
        if (hasFourCards()) {
            // If the other player has four cards, the player with the highest four cards wins
            if (other.hasFourCards()) {
                for (i in 0..4) {
                    val thisValue = getCardValue(originalString[i])
                    val otherValue = getCardValue(other.originalString[i])
                    if (thisValue != otherValue) {
                        return thisValue.compareTo(otherValue)
                    }
                }
            }
            // If the other player has three or fewer cards, we win
            return 1
        }

        // If the other player has four cards, we lose
        if (other.hasFourCards()) return -1

        if (hasFullHouse()) {
            if (other.hasFullHouse()) {
                for (i in 0..4) {
                    val thisValue = getCardValue(originalString[i])
                    val otherValue = getCardValue(other.originalString[i])
                    if (thisValue != otherValue) {
                        return thisValue.compareTo(otherValue)
                    }
                }
            }
            return 1
        }

        if (other.hasFullHouse()) return -1

        if (hasThreeCards()) {
            if (other.hasThreeCards()) {
                for (i in 0..4) {
                    val thisValue = getCardValue(originalString[i])
                    val otherValue = getCardValue(other.originalString[i])
                    if (thisValue != otherValue) {
                        return thisValue.compareTo(otherValue)
                    }
                }
            }
            return 1
        }

        if (other.hasThreeCards()) return -1

        if (hasTwoPairs()) {
            if (other.hasTwoPairs()) {
                for (i in 0..4) {
                    val thisValue = getCardValue(originalString[i])
                    val otherValue = getCardValue(other.originalString[i])
                    if (thisValue != otherValue) {
                        return thisValue.compareTo(otherValue)
                    }
                }
            }
            return 1
        }

        if (other.hasTwoPairs()) return -1

        if (hasOnePair()) {
            if (other.hasOnePair()) {
                for (i in 0..4) {
                    val thisValue = getCardValue(originalString[i])
                    val otherValue = getCardValue(other.originalString[i])
                    if (thisValue != otherValue) {
                        return thisValue.compareTo(otherValue)
                    }
                }
            }
            return 1
        }

        if (other.hasOnePair()) return -1

        for (i in 0..4) {
            val thisValue = getCardValue(originalString[i])
            val otherValue = getCardValue(other.originalString[i])
            if (thisValue != otherValue) {
                return thisValue.compareTo(otherValue)
            }
        }

        error("No winner found: $cards vs ${other.cards}")
    }

    private fun getCardValue(card: Char): Int {
        if (Day07.partB) {
            return cardValuesPartB[card] ?: -1
        }
        return cardValues[card] ?: -1
    }

    private fun hasAllCards(): Boolean {
        return cards.values.all { it == 5 }
    }

    private fun hasFourCards(): Boolean {
        return cards.values.any { it == 4 }
    }

    private fun hasFullHouse(): Boolean {
        return hasThreeCards() && hasTwoCards()
    }

    private fun hasThreeCards(): Boolean {
        return cards.values.any { it == 3 }
    }

    private fun hasTwoCards(): Boolean {
        return cards.values.any { it == 2 }
    }

    private fun hasTwoPairs(): Boolean {
        return cards.values.count { it == 2 } == 2
    }

    private fun hasOnePair(): Boolean {
        return cards.values.count { it == 2 } == 1
    }

    fun useJoker(): Map<Char, Int> {
        val joker = cards.keys.find { it == 'J' } ?: return cards
        val amount = cards[joker] ?: return cards

        val mostCards = cards.keys.filter { it != joker }.sortedByDescending { cards[it] }.firstOrNull() ?: 'A'
        val mostCardAmount = cards[mostCards] ?: 0

        val maxValueCard = cards.filter { it.key != joker && it.value == mostCardAmount }.map { it.key }
            .maxByOrNull { getCardValue(it) } ?: 'A'
        val newCards = cards.toMutableMap()

        newCards.remove(joker)
        newCards[maxValueCard] = newCards.getOrDefault(maxValueCard, 0) + amount
        return newCards
    }
}


fun main() {
    Day07().execute()
}
