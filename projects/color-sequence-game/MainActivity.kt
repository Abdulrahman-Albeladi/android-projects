package com.example.project4

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game = Game(this)

        findViewById<Button>(R.id.magicButtonRed).setOnClickListener {
            handleButtonClick(Color.RED)
        }
        findViewById<Button>(R.id.magicButtonGreen).setOnClickListener {
            handleButtonClick(Color.GREEN)
        }
        findViewById<Button>(R.id.magicButtonYellow).setOnClickListener {
            handleButtonClick(Color.YELLOW)
        }
        findViewById<Button>(R.id.magicButtonBlue).setOnClickListener {
            handleButtonClick(Color.BLUE)
        }
        findViewById<Button>(R.id.magicButtonReset).setOnClickListener {
            resetGame()
        }

        resumeGame()
    }

    private fun resumeGame() {
        game.generateNewLevel()
        displayPatternToast(game.currentS)
    }

    private fun handleButtonClick(color: Color) {
        if (game.checkPattern(color)) {
            if (game.isPatternComplete()) {
                proceedToNextLevel()
            }
        } else {
            handleLoss()
        }
    }

    private fun resetGame() {
        game.resetGame()
        resumeGame()
    }

    private fun proceedToNextLevel() {
        game.moveToNextLevel()
        displayPatternToast(game.currentS)
    }

    private fun handleLoss() {
        Toast.makeText(this, "Game Over. You lost!", Toast.LENGTH_SHORT).show()
        game.resetGame()
        resumeGame()
    }

    private fun displayPatternToast(sequence: List<Color>) {
        val patternMessage = sequence.joinToString(separator = "-") { it.name }
        Toast.makeText(this, patternMessage, Toast.LENGTH_LONG).show()
    }
}
