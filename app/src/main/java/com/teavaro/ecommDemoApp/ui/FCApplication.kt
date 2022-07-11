package com.teavaro.ecommDemoApp.ui

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.swrve.sdk.SwrveSDK
import com.swrve.sdk.config.SwrveConfig
import com.teavaro.ecommDemoApp.BuildConfig

@Suppress("unused")
class FCApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        this.initAppPolices()
//        println("Teavaro:------------------initializing FunnelConnectSDK-${BuildConfig.VERSION_NAME}-------------")
//        FunnelConnectSDK.initialize(this, R.raw.config, FCOptions(true))
        try {
            val config = SwrveConfig()
            config.isAutoStartLastUser = true
            SwrveSDK.createInstance(this, 32153, "FiIpd4eZ8CtQ6carAAx9", config)
        } catch (exp: IllegalArgumentException) {
            Log.e("SwrveDemo", "Could not initialize the Swrve SDK", exp)
        }
    }

    private fun initAppPolices() {
        if (BuildConfig.DEBUG) {
            val threadPolices = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(threadPolices)
        }
    }
}