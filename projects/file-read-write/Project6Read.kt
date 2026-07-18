package com.example.project6.network

import android.graphics.Color
import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Reads a saved record for an email address from a remote endpoint and delivers the parsed response.
 *
 * Endpoint configuration:
 * - The endpoint is provided via constructor and defaults to a non-operational placeholder
 *   (https://example.com/read). This avoids relying on any third-party or course-hosted servers.
 * - Pass a different base URL to the constructor to point at your own backend.
 */
class Project6Read(
    private val callback: (String, List<Any>?, Int?) -> Unit,
    private val endpoint: String = DEFAULT_ENDPOINT
) : AsyncTask<String, Void, Triple<String, List<Any>?, Int?>>() {

    override fun doInBackground(vararg params: String): Triple<String, List<Any>?, Int?> {
        val email = params.firstOrNull() ?: return NOT_FOUND
        val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.name())
        val url = URL("$endpoint?email=$encodedEmail")

        return try {
            (url.openConnection() as HttpURLConnection).run {
                requestMethod = "GET"
                connectTimeout = CONNECT_TIMEOUT_MS
                readTimeout = READ_TIMEOUT_MS

                try {
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        return@run NOT_FOUND
                    }

                    val response = inputStream.bufferedReader().use { it.readText() }
                    parseResponse(response)
                } finally {
                    disconnect()
                }
            }
        } catch (_: Exception) {
            NOT_FOUND
        }
    }

    override fun onPostExecute(result: Triple<String, List<Any>?, Int?>) {
        callback(result.first, result.second, result.third)
    }

    private fun parseResponse(response: String): Triple<String, List<Any>?, Int?> {
        val json = JSONObject(response)
        if (json.optString("found") != "yes") {
            return NOT_FOUND
        }

        val data = json.getJSONArray("data")
        val name = data.getString(0)
        val number = data.getInt(1)
        val colorName = data.getString(2)

        return Triple("yes", listOf(name, number, colorName), colorFor(colorName))
    }

    private fun colorFor(colorName: String): Int? = when (colorName) {
        "yellow" -> Color.YELLOW
        "blue" -> Color.BLUE
        "green" -> Color.GREEN
        "red" -> Color.RED
        "cyan" -> Color.CYAN
        else -> null
    }

    private companion object {
        const val DEFAULT_ENDPOINT = "https://example.com/read"
        const val CONNECT_TIMEOUT_MS = 10_000
        const val READ_TIMEOUT_MS = 10_000

        val NOT_FOUND = Triple("no", null, null)
    }
}
