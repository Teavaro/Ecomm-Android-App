package com.teavaro.ecommDemoApp.debugging

import android.content.Context
import android.content.SharedPreferences
import fi.iki.elonen.NanoHTTPD
import org.json.JSONObject
import androidx.core.content.edit
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.IOException

class SharedPreferencesServer(context: Context, prefsFileName: String) : NanoHTTPD(8080) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val method = session.method
        // Serve other endpoints
        return when {
            uri == "/" -> serveHtml() // Serve UI
            uri == "/data" && method == Method.GET -> serveJson() // Serve JSON data
            uri == "/update" && method == Method.POST -> updateValue(session) // Update value
            uri == "/delete" && method == Method.POST -> deleteValue(session)
            else -> this.createResponse(Response.Status.NOT_FOUND, "text/plain", "404 Not Found")
        }
    }

    private fun serveHtml(): Response {
        val fileName = "sharedPreferenceViewer.html"
        val html = try {
            val inputStream = javaClass.getResourceAsStream("/$fileName")
            inputStream?.bufferedReader()?.use { it.readText() } ?: throw FileNotFoundException("HTML file not found")
        } catch (e: Exception) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Error loading HTML file: ${e.message}")
        }
        return this.createResponse(Response.Status.OK, "text/html", html)
    }

    private fun serveJson(): Response {
        val allPrefs = prefs.all
        val json = JSONObject(allPrefs).toString(4)
        return this.createResponse(Response.Status.OK, "application/json", json)
    }

    private fun updateValue(session: IHTTPSession): Response {
        try {
            // Parse the body to extract parameters
            session.parseBody(null) // Parse form data into parameters
            val key = session.parameters["key"]?.firstOrNull()
            val value = session.parameters["value"]?.firstOrNull()
            if (key != null && value != null) {
                value.toBooleanStrictOrNull()?.let {
                    prefs.edit { putBoolean(key, it) }
                } ?: run {
                    prefs.edit { putString(key, value) }
                }
                return this.createResponse(Response.Status.OK, "text/plain", "Updated")
            } else {
                // If key is not found, return a Bad Request response
                println("❌ Key or value not found in request!")
                return this.createResponse(Response.Status.BAD_REQUEST, "text/plain", "Invalid key or value")
            }
        } catch (e: Exception) {
            // Handle errors that might occur during parsing
            println("⚠️ Error parsing the request body: ${e.message}")
            return this.createResponse(Response.Status.BAD_REQUEST, "text/plain", "Error updating value: ${e.message}")
        }
    }

    private fun deleteValue(session: IHTTPSession): Response {
        try {
            // Parse the body of the request to extract parameters
            session.parseBody(null) // This is required to parse the form data into parameters
            // Retrieve the 'key' parameter from the session's parameters map
            val key = session.parameters["key"]?.firstOrNull()
            // Check if the key exists
            if (key != null) {
                // Proceed with deletion
                prefs.edit { remove(key) }
                return this.createResponse(Response.Status.OK, "text/plain", "Deleted")
            } else {
                // If key is not found, return a Bad Request response
                println("❌ Key not found in request!")
                return this.createResponse(Response.Status.BAD_REQUEST, "text/plain", "Key not found")
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur while parsing the body
            println("⚠️ Error parsing the request body: ${e.message}")
            return this.createResponse(Response.Status.BAD_REQUEST, "text/plain", "Bad Request")
        }
    }

    private fun createResponse(status: Response. IStatus, mimeType: String, message: String) = newFixedLengthResponse(status, mimeType, message)

    companion object {

        private lateinit var server: SharedPreferencesServer

        fun start(context: Context, prefsFileName: String) {
            this.server = SharedPreferencesServer(context, prefsFileName)
            try {
                this.server.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun stop() {
            if (::server.isInitialized)
                this.server.stop()
        }
    }
}