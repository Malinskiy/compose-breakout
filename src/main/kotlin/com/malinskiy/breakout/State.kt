package com.malinskiy.breakout

import androidx.compose.ui.graphics.Color
import com.malinskiy.breakout.Position.Companion.ZERO
import com.malinskiy.breakout.physics.CollisionPhysics
import com.malinskiy.breakout.physics.RectangularBody
import com.malinskiy.breakout.physics.Velocity
import kotlin.math.pow
import kotlin.math.sqrt

class State {
    private var paddle = Paddle(ZERO, ZERO)
    private var ball = Ball(position = Ball.BALL_START)
    private val wallWidth = ((1f / 20f) * Int.MAX_VALUE).toInt()
    private val walls = listOf<Wall>(
        Wall(Position(0, 0), Position(wallWidth, Int.MAX_VALUE)),//Left
        Wall(
            Position(Int.MAX_VALUE - ((1f / 20f) * Int.MAX_VALUE).toInt(), 0),
            Position(Int.MAX_VALUE, Int.MAX_VALUE)
        ),//Right
        Wall(Position(0, Int.MAX_VALUE - wallWidth), Position(Int.MAX_VALUE, Int.MAX_VALUE)),//Bottom
        Wall(Position(0, 0), Position(Int.MAX_VALUE, wallWidth)),//Top
    )
    private var bricks = listOf<Brick>()
    private val collisionPhysics = CollisionPhysics()

    init {
        initState()
    }

    private fun initState() {
        paddle = Paddle.PADDLE_START
        ball.apply {
            val initialVelocity = 0.5
            val x = (0.3)
            val y = sqrt(initialVelocity.pow(2) - x.pow(2))
            body.velocity = Velocity((x * Int.MAX_VALUE).toInt(), (y * Int.MAX_VALUE).toInt())
        }

        val wallMargin = (Int.MAX_VALUE * (1f / 10f)).toInt()
        val margin = (Int.MAX_VALUE * (1f / 100f)).toInt()
        val widthToSplit = Int.MAX_VALUE - 2 * wallMargin
        val brickCountX = 14
        val brickCountY = 8
        val brickWidth = (widthToSplit - margin * (brickCountX - 1)) / brickCountX
        val brickHeight = brickWidth / 3

        bricks = mutableListOf<Brick>().apply {
            for (i in 0 until brickCountX) {
                for (j in 0 until brickCountY) {
                    val color = when(j) {
                        0, 1 -> Color.Red
                        2, 3 -> Color(255, 165, 0)
                        4, 5 -> Color.Green
                        6, 7 -> Color.Yellow
                        else -> Color.White
                    }
                    add(
                        Brick(
                            topLeft = Position(
                                x = wallMargin + i * (margin + brickWidth),
                                y = wallMargin + j * (margin + brickHeight)
                            ),
                            bottomRight = Position(
                                x = wallMargin + brickWidth + i * (margin + brickWidth),
                                y = wallMargin + brickHeight + j * (margin + brickHeight)
                            ),
                            color = color
                        )
                    )
                }
            }
        }.toList()
    }

    fun step(millis: Long) {
        collisionPhysics.process(ball.body, walls.map { it.body } + paddle.body)
        /**
         * Handle collisions for bricks separately
         */
        var brickToRemove = mutableListOf<Brick>()
        collisionPhysics.process(ball.body, bricks.map { it.body }) { body ->
            bricks.find { it.body == body }?.let { 
                brickToRemove.add(it)
            }
        }
        bricks = bricks - brickToRemove
        val position = ball.body.update(ball.position, millis)
        val radius = ball.radius
        val velocity = ball.body.velocity
        ball = Ball(
            radius = ball.radius, position = position, body = RectangularBody(
                topLeft = position.move(-radius, -radius),
                bottomRight = position.move(radius, radius)
            )
        )
        ball.body.velocity = velocity

        paddle = paddle.step()
    }

    fun onRight() {
        paddle.body.velocity = paddle.body.velocity.copy(x = Int.MAX_VALUE / 50)        
//        paddle = paddle.move(x = Int.MAX_VALUE / 30)
    }

    fun onLeft() {
//        paddle = paddle.move(x = -Int.MAX_VALUE / 30)
        paddle.body.velocity = paddle.body.velocity.copy(x = - Int.MAX_VALUE / 50)
    }

    fun onUp() {}

    fun onDown() {}

    fun render(): World {
        return World(
            paddle = paddle,
            ball = ball,
            walls = walls,
            bricks = bricks
        )
    }
}
