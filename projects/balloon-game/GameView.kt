package com.example.project7

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameView(
    context: Context,
    private val hostActivity: AppCompatActivity,
    private val balloonList: List<Balloon>
) : View(context) {

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    private var attemptCount = 0
    private var poppedCount = 0
    private val maxAttemptsAllowed = balloonList.size + 3

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        balloonList
            .filterNot { it.isPopped }
            .forEach { balloon ->
                canvas.drawCircle(
                    balloon.getXPosition().toFloat(),
                    balloon.getYPosition().toFloat(),
                    balloon.getBalloonRadius().toFloat(),
                    paint
                )
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) {
            return true
        }

        val balloon = balloonList.firstOrNull { candidate ->
            !candidate.isPopped && candidate.containsPoint(event.x, event.y)
        }

        if (balloon != null) {
            balloon.popBalloon()
            poppedCount++
            invalidate()

            if (poppedCount == balloonList.size) {
                Toast.makeText(context, "You Won!", Toast.LENGTH_LONG).show()
                hostActivity.finish()
            }

            return true
        }

        attemptCount++
        if (attemptCount >= maxAttemptsAllowed) {
            Toast.makeText(context, "Game Over!", Toast.LENGTH_LONG).show()
            hostActivity.finish()
        }

        return true
    }
}
