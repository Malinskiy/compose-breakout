package com.malinskiy.breakout

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class Position(
    val x: Int,
    val y: Int
) {
    fun scaleToOffset(size: Size) = Offset(
        (x.toFloat() / Int.MAX_VALUE) * (size.width),
        (y.toFloat() / Int.MAX_VALUE) * (size.height),
    )

    fun scaleToSize(size: Size) = Size(
        (x.toFloat() / Int.MAX_VALUE) * (size.width),
        (y.toFloat() / Int.MAX_VALUE) * (size.height),
    )

    fun move(x: Float, y: Float) = move(x.toInt(), y.toInt()) 

    fun move(x: Int, y: Int): Position {
        return Position(this.x + x, this.y + y)
    }

    companion object {
        val ZERO = Position(0, 0)
    }
}

fun Int.scale(size: Size) = (toFloat() / Int.MAX_VALUE) * size.height
