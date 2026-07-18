package com.example.project3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var messageView: TextView
    private lateinit var submitButton: Button
    private lateinit var shiftInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shiftInput = findViewById(R.id.inputNumber)
        submitButton = findViewById(R.id.btnSubmit)
        messageView = findViewById(R.id.tvMessage)

        submitButton.setOnClickListener {
            val shift = shiftInput.text.toString().toIntOrNull()

            when {
                shift == null -> {
                    messageView.text = "Please enter a valid number between 1 and 25"
                }
                shift !in 1..25 -> {
                    messageView.text = "The number has to be between 1 and 25"
                }
                else -> {
                    val intent = Intent(this, EncryptActivity::class.java)
                    intent.putExtra("shiftNumber", shift)
                    startActivity(intent)
                }
            }
        }
    }
}
