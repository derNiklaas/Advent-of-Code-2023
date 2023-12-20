import kotlin.contracts.contract
import utils.AoCDay

class Day19 : AoCDay() {

    private val rules = input.joinToString("\n").split("\n\n")[0].split("\n").map { row ->
        val (name, rules) = row.dropLast(1).split("{")
        val partRules = mutableListOf<PartRule>()
        rules.split(",").forEach {
            if (":" !in it) {
                partRules += PartRule("Ä", '>', 4000, it) { true }
                return@forEach
            }
            val (start, destination) = it.split(":")
            if ("<" in start) {
                val (parameter, value) = start.split("<")
                partRules += PartRule(parameter, '<', value.toInt(), destination) { number ->
                    number < value.toInt()
                }
            } else {
                val (parameter, value) = start.split(">")
                partRules += PartRule(parameter, '>', value.toInt(), destination) { number ->
                    number > value.toInt()
                }
            }
        }
        PartRuleCollection(name, partRules)
    }

    private val parts = input.joinToString("\n").split("\n\n")[1].split("\n").map {
        val values = it.drop(1).dropLast(1).split(",")
        val parsedValues = values.associate { value ->
            val (letter, amount) = value.split("=")
            letter to amount.toInt()
        }
        MachinePart(parsedValues)
    }

    override fun part1(): Any {
        val acceptedParts = mutableListOf<MachinePart>()

        parts.forEach { part ->
            var currentRules = rules.find { it.name == "in" }!!
            while (true) {
                for (rule in currentRules.rules) {
                    val parameter = part.parameters[rule.parameterName] ?: -1
                    if (rule.predicate(parameter)) {
                        if (rule.location == "A") {
                            acceptedParts += part
                            return@forEach
                        }
                        if (rule.location == "R") {
                            return@forEach
                        }
                        currentRules = rules.find { it.name == rule.location }!!
                        break
                    }
                }
            }
        }
        return acceptedParts.sumOf { it.parameters.map { param -> param.value }.sum() }
    }

    override fun part2(): Any {
        val ranges = "xmas".associate { it.toString() to 1..4000 }
        return findRanges(
            ranges, "in"
        ).accepted.sumOf {
            it.values.map { range -> range.last.toLong() - range.first.toLong() + 1 }.reduce(Long::times)
        }
    }

    private fun findRanges(
        ranges: Map<String, IntRange>, workflowName: String
    ): Ranges {
        val workflow = rules.first { it.name == workflowName }

        return workflow.rules.runningFold(Ranges(accepted = listOf(), pending = listOf(ranges))) { combinations, rule ->
            val accepted = combinations.accepted.toMutableList()
            val newPending = mutableListOf<Map<String, IntRange>>()
            combinations.pending.forEach { current ->
                if (rule.parameterName == "Ä") {
                    when (rule.location) {
                        "A" -> accepted.add(current)
                        "R" -> newPending.add(current)
                        else -> {
                            accepted.addAll(findRanges(current, rule.location).accepted)
                        }
                    }
                } else {
                    val value = rule.value
                    val parameterName = rule.parameterName
                    val range = current.getValue(parameterName)
                    val inRange = current.plus(
                        parameterName to if (rule.operator == '>') {
                            maxOf(value + 1, range.first)..range.last
                        } else {
                            range.first..minOf(value - 1, range.last)
                        }
                    )
                    val outOfRange = current.plus(
                        parameterName to if (rule.operator == '>') {
                            range.first..minOf(value, range.last)
                        } else {
                            maxOf(value, range.first)..range.last
                        }
                    )
                    when (rule.location) {
                        "A" -> {
                            accepted += inRange
                            newPending += outOfRange
                        }

                        "R" -> newPending += outOfRange
                        else -> {
                            accepted += findRanges(ranges = inRange, workflowName = rule.location).accepted
                            newPending += outOfRange
                        }
                    }
                }

            }
            Ranges(accepted, newPending)
        }.last()
    }
}

private data class MachinePart(val parameters: Map<String, Int>)
private data class PartRuleCollection(val name: String, val rules: List<PartRule>)
private data class PartRule(
    val parameterName: String, val operator: Char, val value: Int, val location: String, val predicate: (Int) -> Boolean
)

private data class Ranges(val accepted: List<Map<String, IntRange>>, val pending: List<Map<String, IntRange>>)

fun main() {
    Day19().execute()
}
