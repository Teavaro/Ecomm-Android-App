package com.teavaro.ecommDemoApp.core.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.funnelConnect.initializer.FunnelConnectSDK

object TrackUtils  : LifecycleObserver {
    const val IMPRESSION = "impression"
    const val CLICK = "click"
    const val GEO_PLACE = "geo_place"
    const val ABANDONED_CART_ID = "abandoned_cart_id"

    fun impression(value: String) {
        logEvent {
            FunnelConnectSDK.logEvent(IMPRESSION, value)
        }
    }

    fun click(value: String){
        logEvent {
            FunnelConnectSDK.logEvent(CLICK, value)
        }
    }

    fun events(events: Map<String, String>){
        logEvent {
            FunnelConnectSDK.logEvents(events)
        }
    }

    fun geoPlace(value: String){
        logEvent {
            FunnelConnectSDK.logEvent(GEO_PLACE, value)
        }
    }

    fun logEvent(action: (()->Unit)){
        if(FunnelConnectSDK.isInitialized() && Store.isNbaPermissionAccepted()){
            action.invoke()
        }
    }

    fun lifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(object: LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        impression("on_resume_scene")
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        impression("on_pause_scene")
                    }
                    Lifecycle.Event.ON_CREATE -> {
                        impression("on_create_scene")
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        impression("on_destroy_scene")
                    }
                    else -> { }
                }
            }
        })
    }
}