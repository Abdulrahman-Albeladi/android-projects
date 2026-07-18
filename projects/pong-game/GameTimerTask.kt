package com.example.project5

import android.os.Handler
import android.os.Looper
import java.util.TimerTask

/** Posts game updates to the main thread when invoked by a timer. */
class GameTimerTask(private val gameView: GameView) : TimerTask() {
    private val handler = Handler(Looper.getMainLooper())

    override fun run() {
        handler.post { gameView.updateGame() }
    }
}
