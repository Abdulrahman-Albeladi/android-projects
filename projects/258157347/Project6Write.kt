package com.example.project6.network

import android.net.Uri
import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Project6Write(
    private val callback: (String) -> Unit,
    private val endpoint: String = DEFAULT_ENDPOINT
) : AsyncTask<Any, Void, String>() {

    override fun doInBackground(vararg params: Any): String {
        val email = params.getOrNull(0) as? String ?: return failureResponse()
        val name = params.getOrNull(1) as? String ?: return failureResponse()
        val number = params.getOrNull(2) as? Int ?: return failureResponse()

        val payload = JSONObject()
            .put("email", email)
            .put("name", name)
            .put("number", number)
            .toString()

        val requestUrl = Uri.parse(endpoint)
            .buildUpon()
            .appendQueryParameter("data", payload)
            .build()
            .toString()

        var connection: HttpURLConnection? = null
        return try {
            connection = URL(requestUrl).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                failureResponse()
            } else {
                connection.inputStream.bufferedReader().use { it.readText() }
            }
        } catch (_: Exception) {
            failureResponse()
        } finally {
            connection?.disconnect()
        }
    }

    override fun onPostExecute(result: String) {
        callback(result)
    }

    private fun failureResponse(): String = "{\"result\":\"bad\"}"

    private companion object {
        const val DEFAULT_ENDPOINT =
            "https://cmsc436-2301.cs.umd.edu/project6Write.php"
    }
}
