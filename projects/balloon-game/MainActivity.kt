package com.example.project7

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import javax.xml.parsers.SAXParserFactory

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val balloons = loadBalloons()
        gameView = GameView(this, this, balloons)
        setContentView(gameView)
    }

    private fun loadBalloons(): List<Balloon> {
        val handler = SAXHandler()
        val parser = SAXParserFactory.newInstance().newSAXParser()

        resources.openRawResource(R.raw.balloons3).use { inputStream ->
            parser.parse(inputStream, handler)
        }

        return handler.getBalloons().also { balloons ->
            balloons.forEach { balloon ->
                Log.w(TAG, "Loaded balloon: $balloon")
            }
        }
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}
