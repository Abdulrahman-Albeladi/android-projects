package com.example.project6

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project6.network.Project6Read
import com.example.project6.network.Project6Write

class MainActivity : AppCompatActivity() {

    private lateinit var read: Button
    private lateinit var write: Button
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.tv)
        read = findViewById(R.id.read)
        write = findViewById(R.id.write)

        write.setOnClickListener {
            val contactEmail = contactEmail()
            if (contactEmail == null) {
                text.text = "NA"
                return@setOnClickListener
            }

            Project6Write(this::handleWriteResult).execute(contactEmail, "Phoenix", 20)
        }

        read.setOnClickListener {
            val contactEmail = contactEmail()
            if (contactEmail == null) {
                text.text = "NA"
                return@setOnClickListener
            }

            Project6Read(this::handleReadResult).execute(contactEmail)
        }
    }

    private fun contactEmail(): String? {
        return applicationInfo.metaData
            ?.getString(CONTACT_EMAIL_METADATA_KEY)
            ?.takeIf(String::isNotBlank)
    }

    private fun handleWriteResult(result: String) {
        Log.d(TAG, result)
        text.text = result
    }

    private fun handleReadResult(result: String, data: List<Any>?, color: Int?) {
        Log.d(TAG, result)

        if (result != "yes" || data?.size != 3) {
            text.text = "NA"
            return
        }

        val name = data[0] as? String
        val number = data[1] as? Int
        if (name == null || number == null) {
            text.text = "NA"
            return
        }

        text.text = "$name will be $number years old"
        color?.let(text::setBackgroundColor)
    }

    private companion object {
        const val TAG = "MainActivity"
        const val CONTACT_EMAIL_METADATA_KEY = "com.example.project6.CONTACT_EMAIL"
    }
}
