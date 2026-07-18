package com.example.project5

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val ballPaint = Paint().apply { color = Color.RED }
    private val paddlePaint = Paint().apply { color = Color.BLUE }

    private var paddleX = INITIAL_PADDLE_X
    private var gameEnded = false

    private var ballX = INITIAL_BALL_X
    private var ballY = INITIAL_BALL_Y
    private var ballVelocityX = INITIAL_BALL_VELOCITY
    private var ballVelocityY = INITIAL_BALL_VELOCITY

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(ballX, ballY, BALL_RADIUS, ballPaint)
        canvas.drawRect(paddleX, PADDLE_Y, paddleX + PADDLE_WIDTH, PADDLE_Y + PADDLE_HEIGHT, paddlePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gameEnded) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                restartGame()
            }
            return true
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            paddleX = event.x - PADDLE_WIDTH / 2f
            invalidate()
        }

        return true
    }

    fun updateGame() {
        if (gameEnded) return

        ballX += ballVelocityX
        ballY += ballVelocityY

        if (ballX - BALL_RADIUS < 0f || ballX + BALL_RADIUS > width) {
            ballVelocityX *= -1f
        }

        if (ballY - BALL_RADIUS < 0f) {
            ballVelocityY *= -1f
        }

        if (ballY + BALL_RADIUS > PADDLE_Y && ballX > paddleX && ballX < paddleX + PADDLE_WIDTH) {
            ballVelocityY *= -1f
        }

        if (ballY + BALL_RADIUS > height) {
            gameEnded = true
        }

        invalidate()
    }

    private fun restartGame() {
        ballX = INITIAL_BALL_X
        ballY = INITIAL_BALL_Y
        ballVelocityX = INITIAL_BALL_VELOCITY
        ballVelocityY = INITIAL_BALL_VELOCITY
        gameEnded = false
        invalidate()
    }

    private companion object {
        const val INITIAL_PADDLE_X = 100f
        const val INITIAL_BALL_X = 100f
        const val INITIAL_BALL_Y = 100f
        const val INITIAL_BALL_VELOCITY = 10f
        const val BALL_RADIUS = 20f
        const val PADDLE_Y = 700f
        const val PADDLE_WIDTH = 50f
        const val PADDLE_HEIGHT = 30f
    }
}
