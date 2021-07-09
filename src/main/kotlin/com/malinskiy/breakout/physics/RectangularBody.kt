package com.malinskiy.breakout.physics

import com.malinskiy.breakout.Position
import java.lang.Math.min
import kotlin.math.max

data class RectangularBody(
    val topLeft: Position,
    val bottomRight: Position
) : Body() {
    fun intersect(other: RectangularBody): Boolean {
        return !((topLeft.x > other.bottomRight.x) || (bottomRight.x < other.topLeft.x) || (topLeft.y > other.bottomRight.y) || (bottomRight.y < other.topLeft.y))
    }

    /**
     * Elastic collision with one immovable body
     */
    fun collide(other: RectangularBody) {
        /**
         * Assumption that the other body is parallel to the axis always so we flip the normal velocity component
         */
        val overlapTopLeft = Position(
            x = max(min(topLeft.x, bottomRight.x), min(other.topLeft.x, other.bottomRight.x)),
            y = max(min(topLeft.y, bottomRight.y), min(other.topLeft.y, other.bottomRight.y)),
        )
        val overlapBottomRight = Position(
            x = min(max(topLeft.x, bottomRight.x), max(other.topLeft.x, other.bottomRight.x)),
            y = min(max(topLeft.y, bottomRight.y), max(other.topLeft.y, other.bottomRight.y)),
        )

        val overlapWidth = Math.abs(overlapTopLeft.x - overlapBottomRight.x)
        val overlapHeight = Math.abs(overlapTopLeft.y - overlapBottomRight.y)
        velocity = when {
            overlapWidth > overlapHeight -> velocity.copy(y = -velocity.y) 
            overlapWidth < overlapHeight -> velocity.copy(x = -velocity.x) 
            else -> velocity.copy(x = -velocity.x, y = -velocity.y) 
        }
    }
}
