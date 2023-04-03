package com.teavaro.ecommDemoApp.core.utils

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object PushNotification {

    private const val swrveKeyCampaign = "708f47c5-e22d-457b-9d34-4cd35a160acb"
    private const val URL = "https://service.swrve.com/push?push_key=$swrveKeyCampaign"

    fun send(user: String?, message: String?): String {
        return try {
            if(user != null && message != null) {
                val url = URL("$URL&user=$user")
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                try {
                    val response: InputStream = BufferedInputStream(urlConnection.inputStream)
                    response.toString()
                } finally {
                    urlConnection.disconnect()
                }
            }
            else
                ""
        } catch (e: java.lang.Exception){
            "DemoApp: $e"
        }
    }
}