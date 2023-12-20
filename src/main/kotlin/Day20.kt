import utils.AoCDay

class Day20 : AoCDay() {

    override fun part1(): Any {
        val modules = parse(input)

        var lowPulses = 0L
        var highPulses = 0L

        repeat(1000) {
            val result = pressButton(modules)
            lowPulses += result.lowPulses
            highPulses += result.highPulses
        }

        return lowPulses * highPulses
    }

    override fun part2(): Any {
        val modules = parse(input)

        val conjunctionToRx = modules.single { "rx" in it.destinations }
        val valuesForHighPulse = modules.filter { conjunctionToRx.name in it.destinations }
            .associate { it.name to 0 }
            .toMutableMap()

        var presses = 0
        while(valuesForHighPulse.any { it.value == 0 }) {
            presses++
            pressButton(modules) {conjunctionName ->
                if(conjunctionName in valuesForHighPulse) {
                    valuesForHighPulse[conjunctionName] = presses
                }
            }
        }

        return "https://www.wolframalpha.com/input?i2d=true&i=lcm%5C%2840%29${valuesForHighPulse.values.joinToString("%5C%2844%29")}%5C%2841%29"
    }

    private fun pressButton(
        modules: List<Module>,
        onHighPulseConjunction: (String) -> Unit = {},
    ): ButtonPressResult {
        var lowPulses = 0
        var highPulses = 0
        val byName = modules.associateBy { it.name }
        val queue = mutableListOf(Instruction(low = true, from = "button", to = "broadcaster"))
        while (queue.isNotEmpty()) {
            val instruction = queue.removeFirst()

            if (instruction.low) {
                lowPulses++
            } else {
                highPulses++
            }

            when (val module = byName[instruction.to]) {
                is Module.Broadcaster -> {
                    module.destinations.mapTo(queue) { destination ->
                        Instruction(low = instruction.low, to = destination, from = module.name)
                    }
                }

                is Module.Conjunction -> {
                    module.memory[instruction.from] = !instruction.low
                    val hasOnlyHighPulses = module.memory.all { it.value }
                    if (!hasOnlyHighPulses) {
                        onHighPulseConjunction(module.name)
                    }
                    module.destinations.mapTo(queue) { destination ->
                        Instruction(low = hasOnlyHighPulses, to = destination, from = module.name)
                    }
                }

                is Module.FlipFlow -> {
                    if (instruction.low) {
                        module.on = !module.on
                        module.destinations.mapTo(queue) { destination ->
                            Instruction(low = !module.on, to = destination, from = module.name)
                        }
                    }
                }

                null -> {
                    // rx is important for part b, but not a module on its own
                    if(instruction.to == "rx") continue
                    error("unknown module ${instruction.to}")
                }
            }
        }
        return ButtonPressResult(lowPulses, highPulses)
    }

    private fun parse(input: List<String>): List<Module> {
        val modules = input.map { line ->
            val (moduleValue, destinationsValue) = line.split(" -> ")
            val destinations = destinationsValue.split(", ")
            when {
                moduleValue.startsWith('%') -> Module.FlipFlow(
                    on = false,
                    name = moduleValue.drop(1),
                    destinations = destinations,
                )

                moduleValue == "broadcaster" -> Module.Broadcaster(destinations)
                moduleValue.startsWith("&") -> Module.Conjunction(
                    name = moduleValue.drop(1),
                    destinations = destinations,
                    memory = mutableMapOf(),
                )

                else -> error("Invalid module $moduleValue")
            }
        }

        modules.forEach { module ->
            if (module is Module.Conjunction) {
                modules.forEach { other ->
                    if (module.name in other.destinations) {
                        module.memory[other.name] = false
                    }
                }
            }
        }

        return modules
    }
}

private data class Instruction(val low: Boolean, val from: String, val to: String)
private data class ButtonPressResult(val lowPulses: Int, val highPulses: Int)
private sealed interface Module {
    val name: String
    val destinations: List<String>

    data class FlipFlow(
        var on: Boolean,
        override val name: String,
        override val destinations: List<String>,
    ) : Module

    data class Conjunction(
        override val name: String,
        override val destinations: List<String>,
        val memory: MutableMap<String, Boolean>,
    ) : Module

    data class Broadcaster(override val destinations: List<String>) : Module {
        override val name: String = "broadcaster"
    }
}


fun main() {
    Day20().execute()
}