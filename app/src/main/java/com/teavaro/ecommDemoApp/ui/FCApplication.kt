package com.teavaro.ecommDemoApp.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.StrictMode
import android.util.Log
import com.google.firebase.FirebaseApp
import com.swrve.sdk.SwrveInitMode
import com.swrve.sdk.SwrveNotificationConfig
import com.swrve.sdk.SwrveSDK
import com.swrve.sdk.config.SwrveConfig
import com.swrve.sdk.geo.SwrveGeoConfig
import com.swrve.sdk.geo.SwrveGeoSDK
import com.teavaro.ecommDemoApp.BuildConfig
import com.teavaro.ecommDemoApp.R
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK
import com.teavaro.funnelConnect.data.models.dataClasses.FCOptions

@Suppress("unused")
class FCApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        this.initAppPolices()
        println("Teavaro:------------------initializing FunnelConnectSDK-${BuildConfig.VERSION_NAME}-------------")
        FunnelConnectSDK.initialize(this, "tmi123", FCOptions(true))
        FirebaseApp.initializeApp(this)
        initSwrve()
    }

    private fun initAppPolices() {
        if (BuildConfig.DEBUG) {
            val threadPolices = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(threadPolices)
        }
    }

    private fun initSwrve(){
        try {
            val config = SwrveConfig()
                config.initMode = SwrveInitMode.MANAGED
                config.isAutoStartLastUser = true
            var channel: NotificationChannel? = null
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = NotificationChannel("123", "Devapp swrve default channel", NotificationManager.IMPORTANCE_DEFAULT);
                if (getSystemService(Context.NOTIFICATION_SERVICE) != null) {
                    val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel);
                }
            }
            val notificationConfig: SwrveNotificationConfig.Builder = SwrveNotificationConfig.Builder(R.drawable.burst, R.drawable.crinklys, channel)
                .activityClass(MainActivity::class.java)
                    .largeIconDrawableId(R.drawable.grapes)
                    .accentColorHex("#3949AB")
            config.notificationConfig = notificationConfig.build()
            SwrveSDK.createInstance(this, 32153, "FiIpd4eZ8CtQ6carAAx9", config)
            //geo config
            val geoConfig = SwrveGeoConfig.Builder()
                .geofenceTransitionListener { name: String?, transition: String?, triggerLocation: Location?, customProperties: String? ->
                    FunnelConnectSDK.cdp().logEvent("entryGeoPlace", name.toString())
                }
                .build()
            SwrveGeoSDK.init(this, geoConfig)
        } catch (exp: IllegalArgumentException) {
            Log.e("SwrveDemo", "Could not initialize the Swrve SDK", exp)
        }
    }
}