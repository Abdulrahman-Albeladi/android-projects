package com.example.project5

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var pong: Pong
    private lateinit var gameView: GameView
    private lateinit var gameTimer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pong = Pong()
        gameView = findViewById(R.id.gameView)

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        pong.best = sharedPreferences.getInt(PREFERENCE_BEST_SCORE, 0)

        gameView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (pong.gameEnded) {
                        pong.reset()
                        startGame()
                    }
                }

                MotionEvent.ACTION_MOVE -> pong.movePaddle(event.x)
            }
            true
        }

        startGame()
    }

    private fun startGame() {
        if (::gameTimer.isInitialized) {
            gameTimer.cancel()
        }

        val mainHandler = Handler(Looper.getMainLooper())
        gameTimer = Timer()
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                mainHandler.post { gameView.updateGame() }
            }
        }, 0L, FRAME_INTERVAL_MS)
    }

    override fun onPause() {
        super.onPause()

        if (::gameTimer.isInitialized) {
            gameTimer.cancel()
        }

        getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putInt(PREFERENCE_BEST_SCORE, pong.best)
            .apply()
    }

    private companion object {
        const val PREFERENCE_BEST_SCORE = "Your best score:"
        const val FRAME_INTERVAL_MS = 30L
    }
}
