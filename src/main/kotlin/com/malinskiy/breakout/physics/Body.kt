package com.malinskiy.breakout.physics

import com.malinskiy.breakout.Position

abstract class Body {
    var velocity: Velocity = Velocity(0, 0)
    private var acceleration: Acceleration = Acceleration(0, 0)
    private var force: Force = Force(0, 0)

    fun update(position: Position, millis: Long): Position {
        return Position(
            (position.x + velocity.x * (millis.toFloat() / 1000)).toInt(),
            (position.y + velocity.y * (millis.toFloat() / 1000)).toInt()
        )
    }
}

data class Velocity(val x: Int, val y: Int)
data class Acceleration(val x: Int, val y: Int)
data class Force(val x: Int, val y: Int)
