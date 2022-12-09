package com.teavaro.ecommDemoApp.core

import android.os.AsyncTask

class HTTPAsyncTask(private val action: (()->Unit)) : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg items: String?): String {
        return PushNotification.send(items[0], items[1])
    }
    override fun onPostExecute(result: String?) {
        action.invoke()
    }
}