package com.example.testlayout

import java.util.TimerTask

/**
 * Periodically records velocity measurements for the active run selection.
 */
class LogTask(
    private val selection: RunFragment.sel
) : TimerTask() {

    override fun run() {
        selection.logVelocity()
        selection.logAvgVelocity()
    }
}
