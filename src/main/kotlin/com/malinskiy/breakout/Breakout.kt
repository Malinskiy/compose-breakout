package com.malinskiy.breakout

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.application
import com.malinskiy.breakout.physics.RectangularBody
import kotlinx.coroutines.delay

val state = State()

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(
        title = "Breakout using Jetpack Compose",
        resizable = false,
        size = IntSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT),
        centered = true
    ) {
        DesktopMaterialTheme(
            colors = darkColors(background = Config.BACKGROUND_COLOR)
        ) {
            val world = remember { mutableStateOf(state.render()) }

            LocalAppWindow.current.keyboard.onKeyEvent = {
                var handled = false
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.DirectionUp -> {
                            handled = true
                            state.onUp()
                        }
                        Key.DirectionDown -> {
                            handled = true
                            state.onDown()
                        }
                        Key.DirectionRight -> {
                            handled = true
                            state.onRight()
                        }
                        Key.DirectionLeft -> {
                            handled = true
                            state.onLeft()
                        }
                    }
                    world.value = state.render()
                }
                handled
            }

            BreakoutApp(world.value)

            LaunchedEffect(Unit) {
                var time = System.currentTimeMillis()
                while (true) {
                    delay(GAME_SPEED.toLong())
                    var current = System.currentTimeMillis()
                    val delta = current - time
                    time = current
                    state.step(delta)
                    world.value = state.render()
                }
            }
        }
    }
}

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreakoutApp(world: World) {
    BreakoutWindow(world)
}

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreakoutWindow(world: World) {
    Surface {
        Canvas(modifier = Modifier.fillMaxSize()) {
            Paddle(world.paddle)
            Ball(world.ball)
            Bricks(world.bricks)

            if (Config.debug) {
                for (wall in world.walls) {
                    RectangularBody(wall.body)
                }
            }

            Background()
        }
    }
}

fun DrawScope.Ball(ball: Ball) {
    drawCircle(
        color = Config.BALL_BODY_COLOR,
        radius = ball.radius.scale(size),
        center = ball.position.scaleToOffset(size)
    )
    if (Config.debug) {
        RectangularBody(ball.body)
    }
}

fun DrawScope.Paddle(paddle: Paddle) {
    drawRect(
        color = Config.PADDLE_BODY_COLOR,
        topLeft = paddle.topLeft.scaleToOffset(size),
        size = Position(
            paddle.bottomRight.x - paddle.topLeft.x,
            paddle.bottomRight.y - paddle.topLeft.y
        ).scaleToSize(size)
    )
    if (Config.debug) {
        RectangularBody(paddle.body)
    }
}

fun DrawScope.Bricks(bricks: List<Brick>) {
    bricks.forEach {
        drawRect(
            color = it.color,
            topLeft = it.topLeft.scaleToOffset(size),
            size = Position(
                it.bottomRight.x - it.topLeft.x,
                it.bottomRight.y - it.topLeft.y
            ).scaleToSize(size),
            colorFilter = ColorFilter.tint(Color.LightGray, BlendMode.Multiply)
        )
    }
    if (Config.debug) {
        bricks.forEach {
            RectangularBody(it.body)
        }
    }
}

fun DrawScope.Background() {
    drawCircle(Color.Yellow, center = Offset(0f, 0f), alpha = 0.1f, radius = size.minDimension / 3f)
    drawCircle(Color.Cyan, center = Offset(size.width, 0f), alpha = 0.1f, radius = size.minDimension / 3f)
}

private fun DrawScope.RectangularBody(body: RectangularBody) {
    drawRect(
        color = Color.Red,
        topLeft = body.topLeft.scaleToOffset(size),
        size = Position(
            body.bottomRight.x - body.topLeft.x,
            body.bottomRight.y - body.topLeft.y
        ).scaleToSize(size),
        style = Stroke()
    )
}
