package com.example.testlayout

import java.util.TimerTask

/**
 * Periodically records current and average velocity values for a run.
 */
class LogTask(
    private val selection: RunFragment.sel
) : TimerTask() {

    override fun run() {
        selection.logVelocity()
        selection.logAvgVelocity()
    }
}
