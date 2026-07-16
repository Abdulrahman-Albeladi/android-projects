package com.example.testlayout

import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date

class StopwatchRunnable(
    @Suppress("UNUSED_PARAMETER") fragment: RunFragment,
    private val stopwatchTextView: TextView
) : Runnable {
    @Volatile
    private var paused = false

    private val pauseLock = java.lang.Object()
    private val timeFormatter = SimpleDateFormat("mm:ss:SS")

    var totalTime: String = ""

    init {
        pause()
    }

    fun pause() {
        paused = true
    }

    fun resume() {
        synchronized(pauseLock) {
            paused = false
            pauseLock.notifyAll()
        }
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            if (paused) {
                try {
                    synchronized(pauseLock) {
                        while (paused) {
                            pauseLock.wait()
                        }
                    }
                } catch (_: InterruptedException) {
                    paused = true
                    totalTime = stopwatchTextView.text.toString()
                    totalRuns += 1
                    return
                }
            } else {
                stopwatchTextView.text = timeFormatter.format(
                    Date(SystemClock.currentThreadTimeMillis())
                )
                totalTime = stopwatchTextView.text.toString()
            }
        }

        Log.w("RunFragment", "exited")
    }

    companion object {
        var totalRuns: Int = 0
    }
}
