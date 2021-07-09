package com.malinskiy.breakout

import androidx.compose.ui.graphics.Color
import com.malinskiy.breakout.physics.RectangularBody

data class World(
    val paddle: Paddle,
    val ball: Ball,
    val walls: List<Wall>,
    val bricks: List<Brick>,
)

data class Wall(
    val topLeft: Position,
    val bottomRight: Position,
    val body: RectangularBody = RectangularBody(topLeft, bottomRight)
)

data class Paddle(
    val topLeft: Position,
    val bottomRight: Position,
    val body: RectangularBody = RectangularBody(topLeft, bottomRight)
) {
    fun step(): Paddle {
        val dx = body.velocity.x
        val topLeft = topLeft.move(dx, 0)
        val bottomRight = bottomRight.move(dx, 0)
        val newBody = RectangularBody(topLeft = topLeft, bottomRight = bottomRight).apply { 
            velocity = body.velocity.copy(x = dx * 2 /3)
        }
        return Paddle(topLeft, bottomRight, newBody)
    }

    companion object {
        const val WIDTH = (1f / 15f) * (Int.MAX_VALUE)
        const val HEIGHT = (1f / 45f) * (Int.MAX_VALUE)
        val PADDLE_START = Paddle(
            topLeft = Position(Int.MAX_VALUE / 2, (Int.MAX_VALUE / 8) * 7).move(x = -WIDTH / 2, y = -HEIGHT / 2),
            bottomRight = Position(Int.MAX_VALUE / 2, (Int.MAX_VALUE / 8) * 7).move(x = WIDTH / 2, y = HEIGHT / 2)
        )
    }
}

data class Ball(
    val radius: Int = RADIUS,
    var position: Position = Position.ZERO,
    val body: RectangularBody = RectangularBody(
        topLeft = position.move(-radius, -radius),
        bottomRight = position.move(radius, radius)
    )
) {
    companion object {
        val RADIUS = ((1f / 100f) * (Int.MAX_VALUE)).toInt()
        val BALL_START = Position(Int.MAX_VALUE / 2, (Int.MAX_VALUE / 2))
    }
}

data class Brick(
    val topLeft: Position,
    val bottomRight: Position,
    val color: Color,
    val body: RectangularBody = RectangularBody(topLeft, bottomRight)
)
