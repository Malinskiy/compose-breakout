package com.malinskiy.breakout.physics

class CollisionPhysics {
    /**
     * Simplified version of collision detection because we have only only one moving body
     */
    fun process(ball: RectangularBody, bodies: Collection<RectangularBody>, block: (RectangularBody) -> Unit = {}) {
        for(body in bodies) {
            if(ball.intersect(body)) {
                ball.collide(body)
                block(body)
            }
        }
    }
}
