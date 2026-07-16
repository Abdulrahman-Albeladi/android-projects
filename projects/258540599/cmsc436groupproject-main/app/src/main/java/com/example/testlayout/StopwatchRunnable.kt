package com.example.testlayout

import android.os.SystemClock
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date

class StopwatchRunnable(
    fragment: RunFragment,
    private val stopwatchTextView: TextView
) : Runnable {
    private val pauseLock = Object()

    @Volatile
    private var paused = true

    private val timeFormat = SimpleDateFormat("mm:ss:SS")

    @Volatile
    var totalTime: String = ""
        private set

    fun pause() {
        synchronized(pauseLock) {
            paused = true
        }
    }

    fun resume() {
        synchronized(pauseLock) {
            paused = false
            pauseLock.notifyAll()
        }
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                synchronized(pauseLock) {
                    while (paused) {
                        pauseLock.wait()
                    }
                }
            } catch (exception: InterruptedException) {
                synchronized(pauseLock) {
                    paused = true
                }
                totalTime = stopwatchTextView.text.toString()
                recordCompletedRun()
                Thread.currentThread().interrupt()
                return
            }

            val formattedTime = timeFormat.format(Date(SystemClock.currentThreadTimeMillis()))
            stopwatchTextView.text = formattedTime
            totalTime = formattedTime
        }
    }

    private fun recordCompletedRun() {
        synchronized(StopwatchRunnable::class.java) {
            totalRuns += 1
        }
    }

    companion object {
        var totalRuns: Int = 0
            private set
    }
}
