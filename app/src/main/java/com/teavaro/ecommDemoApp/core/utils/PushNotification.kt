package com.teavaro.ecommDemoApp.core.utils

import com.teavaro.ecommDemoApp.core.Store
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object PushNotification {

    private const val shopKeyCampaign = "708f47c5-e22d-457b-9d34-4cd35a160acb"
    private const val crilklysKeyCampaign = "28eb267d-f758-4418-bf26-93dfda80c581"
    private const val paprikaKeyCampaign = "27e0048f-1200-44be-b3e1-fed6eecd437a"
    private const val watermelonKeyCampaign = "76066617-d885-47c0-a01d-04d00381f254"
    private const val acKeyCampaign = "cf0d9bc0-cf5e-4687-b5d2-c75d0ddc8245"
    private const val identClickKeyCampaign = "47399c46-d27c-4679-97b8-70e0bc5dc91d"
    private const val URL = "https://service.swrve.com/push"

    fun send(address: String, action: (()->Unit)) {
        return background{
            try {
                val url = URL(address)
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                try {
                    val response: InputStream = BufferedInputStream(urlConnection.inputStream)
                    response.toString()
                    action.invoke()
                } finally {
                    urlConnection.disconnect()
                }
            } catch (e: java.lang.Exception){
                "Response:$e"
            }
        }
    }

    fun sendCrilklys(user: String, action: (()->Unit)) {
        send("$URL?push_key=$crilklysKeyCampaign&user=$user&data_template={\"item_id\":\"0\"}", action)
    }

    fun sendPaprika(user: String, action: (()->Unit)) {
        send("$URL?push_key=$paprikaKeyCampaign&user=$user&data_template={\"item_id\":\"3\"}", action)
    }

    fun sendWatermelon(user: String, action: (()->Unit)) {
        send("$URL?push_key=$watermelonKeyCampaign&user=$user&data_template={\"item_id\":\"5\"}", action)
    }

    fun sendAbandonedCart(user: String, action: (()->Unit)) {
        Store.getAbCartId()?.let {
            send("$URL?push_key=$acKeyCampaign&user=$user&data_template={\"ab_cart_id\":\"$it\"}", action)
        }
    }

    fun sendIdentClick(user: String, action: (()->Unit)) {
        Store.getUserId()?.let {
            send("$URL?push_key=$identClickKeyCampaign&user=$user&data_template={\"userr_id\":\"$it\"}", action)
        }
    }

    fun sendShop(user: String, action: (()->Unit)) {
        send("$URL?push_key=$shopKeyCampaign&user=$user", action)
    }

    fun background(action: () -> Unit){
        Thread{
            action.invoke()
        }.start()
    }

    fun crilklysPush(){

    }
}