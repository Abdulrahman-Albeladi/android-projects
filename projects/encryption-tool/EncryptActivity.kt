package com.example.project3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EncryptActivity : AppCompatActivity() {

    private var shiftNumber: Int = 0
    private lateinit var encryptButton: Button
    private lateinit var backButton: Button
    private lateinit var inputText: EditText
    private lateinit var encryptedText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encrypt)

        shiftNumber = intent.getIntExtra(EXTRA_SHIFT_NUMBER, 0)
        inputText = findViewById(R.id.text)
        encryptButton = findViewById(R.id.button1)
        backButton = findViewById(R.id.button2)
        encryptedText = findViewById(R.id.newText)

        encryptButton.setOnClickListener {
            encryptedText.text = Encryption(shiftNumber).encryptText(inputText.text.toString())
        }

        backButton.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    putExtra(EXTRA_SHIFT_NUMBER, shiftNumber)
                }
            )
        }
    }

    private companion object {
        const val EXTRA_SHIFT_NUMBER = "shiftNumber"
    }
}
