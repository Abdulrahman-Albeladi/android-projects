package com.example.project5

/**
 * Maintains the state and collision rules for a simple paddle-and-ball game.
 */
class Pong {
    var ballX = INITIAL_BALL_POSITION
    var ballY = INITIAL_BALL_POSITION
    var ballVelocityX = INITIAL_BALL_VELOCITY
    var ballVelocityY = INITIAL_BALL_VELOCITY
    var paddlePosX = INITIAL_PADDLE_POSITION
    var current = 0
    var best = 0
    var gameEnded = false

    fun reset() {
        ballX = INITIAL_BALL_POSITION
        ballY = INITIAL_BALL_POSITION
        ballVelocityX = INITIAL_BALL_VELOCITY
        ballVelocityY = INITIAL_BALL_VELOCITY
        current = 0
        gameEnded = false
    }

    fun movePaddle(x: Float) {
        paddlePosX = x - PADDLE_HALF_WIDTH
    }

    fun updateBall(viewWidth: Int, viewHeight: Int) {
        if (gameEnded) return

        ballX += ballVelocityX
        ballY += ballVelocityY

        if (ballX - BALL_RADIUS < 0f || ballX + BALL_RADIUS > viewWidth) {
            ballVelocityX *= -1f
        }

        if (ballY - BALL_RADIUS < 0f) {
            ballVelocityY *= -1f
        }

        if (
            ballY + BALL_RADIUS > PADDLE_Y &&
            ballX > paddlePosX &&
            ballX < paddlePosX + PADDLE_WIDTH
        ) {
            ballVelocityY *= -1f
            current += 1
        }

        if (ballY + BALL_RADIUS > viewHeight) {
            gameEnded = true
            if (current > best) {
                best = current
            }
        }
    }

    private companion object {
        const val INITIAL_BALL_POSITION = 100f
        const val INITIAL_BALL_VELOCITY = 10f
        const val INITIAL_PADDLE_POSITION = 100f
        const val BALL_RADIUS = 25f
        const val PADDLE_WIDTH = 50f
        const val PADDLE_HALF_WIDTH = PADDLE_WIDTH / 2f
        const val PADDLE_Y = 700f
    }
}
