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
        // We have 5 of the same card
        if (hasAllCards()) {
            // The other has 5 as well
            if (other.hasAllCards()) {
                return compareAllCards(other)
            }
            // The other has 4 or fewer of the same card
            return 1
        }

        // We don't have 5 of the same card, but the other does
        if (other.hasAllCards()) return -1

        // We have 4 of the same card
        if (hasFourCards()) {
            // The other has 4 as well
            if (other.hasFourCards()) {
                return compareAllCards(other)
            }
            // The other has 3 or fewer of the same card
            return 1
        }

        // We don't have 4 of the same card, but the other does
        if (other.hasFourCards()) return -1

        // We have a full house
        if (hasFullHouse()) {
            // The other has a full house as well
            if (other.hasFullHouse()) {
                return compareAllCards(other)
            }
            return 1
        }

        // The other has a full house
        if (other.hasFullHouse()) return -1

        // We have 3 of the same card
        if (hasThreeCards()) {
            // The other has 3 as well
            if (other.hasThreeCards()) {
                return compareAllCards(other)
            }
            // The other has 2 or fewer of the same card
            return 1
        }

        // We don't have 3 of the same card, but the other does
        if (other.hasThreeCards()) return -1

        // We have 2 pairs of the same card (e.g. 11 and 22)
        if (hasTwoPairs()) {
            // The other has 2 pairs as well
            if (other.hasTwoPairs()) {
                return compareAllCards(other)
            }
            // The other has 1 pair or all cards are different
            return 1
        }

        // The other has 2 pairs
        if (other.hasTwoPairs()) return -1

        // We have 1 pair of the same card (e.g. 11)
        if (hasOnePair()) {
            // The other has 1 pair as well
            if (other.hasOnePair()) {
                return compareAllCards(other)
            }
            // The other has all cards different
            return 1
        }

        // The other has 1 pair
        if (other.hasOnePair()) return -1

        // All cards are unique, so we compare them by string
        return compareAllCards(other)
    }

    private fun compareAllCards(other: CardRound): Int {
        for (i in 0..4) {
            val thisValue = getCardValue(originalString[i])
            val otherValue = getCardValue(other.originalString[i])
            if (thisValue != otherValue) {
                return thisValue.compareTo(otherValue)
            }
        }
        error("Could not compare cards $cards vs ${other.cards}")
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
