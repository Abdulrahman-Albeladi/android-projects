package com.example.project4

import android.content.Context
import android.content.SharedPreferences

/**
 * Maintains the sequence-matching game state and persists the highest level reached.
 */
class Game(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private var bestLevel: Int = preferences.getInt(BEST_LEVEL_KEY, 0)

    var current: Int = 1
        private set

    private val targetSequence = mutableListOf<Color>()
    var currentS: MutableList<Color> = mutableListOf()
        private set
    private val expectedSequence = mutableListOf<Color>()
    private val userSequence = mutableListOf<Color>()

    fun generateNewLevel() {
        currentS.clear()
        targetSequence.clear()
        expectedSequence.clear()

        targetSequence.add(Color.RED)
        currentS.addAll(targetSequence)
        expectedSequence.addAll(targetSequence)
    }

    /**
     * Records a correct selection and returns whether it matches the next expected color.
     */
    fun checkPattern(color: Color): Boolean {
        if (userSequence.size >= expectedSequence.size) {
            return false
        }

        val expectedColor = expectedSequence[userSequence.size]
        if (color != expectedColor) {
            return false
        }

        userSequence.add(color)
        return true
    }

    fun isPatternComplete(): Boolean = userSequence.size == expectedSequence.size

    fun moveToNextLevel() {
        current++
        if (current > bestLevel) {
            bestLevel = current
            saveBestLevel()
        }

        userSequence.clear()
        generateNewLevel()
    }

    fun resetGame() {
        current = 1
        bestLevel = 0
        userSequence.clear()
        generateNewLevel()
    }

    private fun saveBestLevel() {
        preferences.edit().putInt(BEST_LEVEL_KEY, bestLevel).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "GamePrefs"
        const val BEST_LEVEL_KEY = "bestL"
    }
}

enum class Color {
    RED,
    GREEN,
    YELLOW,
    BLUE,
}
