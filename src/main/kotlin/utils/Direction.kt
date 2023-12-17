package utils

enum class Direction() {

    UP {
        override val left get() = LEFT
        override val right get() = RIGHT
        override val opposite get() = DOWN
    },
    RIGHT {
        override val left get() = UP
        override val right get() = DOWN
        override val opposite get() = LEFT
    },
    DOWN {
        override val left get() = RIGHT
        override val right get() = LEFT
        override val opposite get() = UP
    },
    LEFT {
        override val left get() = DOWN
        override val right get() = UP
        override val opposite get() = RIGHT
    };

    abstract val left: Direction
    abstract val right: Direction
    abstract val opposite: Direction

    val pointPositiveDown: Vec2D
        get() = DirectionPointMapping.downPositive[this]

}

class DirectionPointMapping(positiveY: Direction, positiveX: Direction) {
    private val up = if (positiveY == Direction.UP) Vec2D(0, 1) else Vec2D(0, -1)
    private val down = Vec2D(0, -up.y)
    private val left = if (positiveX == Direction.LEFT) Vec2D(1, 0) else Vec2D(-1, 0)
    private val right = Vec2D(-left.x, 0)

    operator fun get(direction: Direction): Vec2D {
        return when (direction) {
            Direction.UP -> up
            Direction.DOWN -> down
            Direction.LEFT -> left
            Direction.RIGHT -> right
        }
    }

    companion object {
        val downPositive = DirectionPointMapping(Direction.DOWN, Direction.RIGHT)
    }
}