import kotlin.contracts.contract
import utils.AoCDay

class Day19 : AoCDay() {

    private val rules = input.joinToString("\n").split("\n\n")[0].split("\n").map { row ->
        val (name, rules) = row.dropLast(1).split("{")
        val partRules = mutableListOf<PartRule>()
        rules.split(",").forEach {
            if (":" !in it) {
                partRules += PartRule("Ä", it) { true }
                return@forEach
            }
            val (start, destination) = it.split(":")
            if ("<" in start) {
                val (parameter, value) = start.split("<")
                partRules += PartRule(parameter, destination) { number ->
                    number < value.toLong()
                }
            } else {
                val (parameter, value) = start.split(">")
                partRules += PartRule(parameter, destination) { number ->
                    number > value.toLong()
                }
            }
        }
        PartRuleCollection(name, partRules)
    }

    private val parts = input.joinToString("\n").split("\n\n")[1].split("\n").map {
        val values = it.drop(1).dropLast(1).split(",")
        val parsedValues = values.map { value ->
            val (letter, amount) = value.split("=")
            letter to amount.toLong()
        }.toMap()
        MachinePart(parsedValues)
    }

    override fun part1(): Any {
        val acceptedParts = mutableListOf<MachinePart>()

        parts.forEach { part ->
            var currentRules = rules.find { it.name == "in" }!!
            while (true) {
                for (rule in currentRules.rules) {
                    val parameter = part.parameters[rule.parameterName] ?: -1
                    if(rule.predicate(parameter)) {
                        if (rule.location == "A") {
                            println("$part: A")
                            acceptedParts += part
                            return@forEach
                        }
                        if (rule.location == "R") {
                            println("$part: R")
                            return@forEach
                        }
                        currentRules = rules.find { it.name == rule.location }!!
                        ///println("$part: ${currentRules.name}")
                        break
                    }
                }
            }
        }
        return acceptedParts.sumOf { it.parameters.map { param -> param.value }.sum() }
    }

    override fun part2(): Any {
        TODO("Not yet implemented")
    }
}

private data class MachinePart(val parameters: Map<String, Long>)
private data class PartRuleCollection(val name: String, val rules: List<PartRule>)
private data class PartRule(val parameterName: String, val location: String, val predicate: (Long) -> Boolean)

fun main() {
    Day19().execute()
}
