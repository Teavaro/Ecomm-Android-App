package com.teavaro.ecommDemoApp.core

import android.content.Context

object SharedPreferenceUtils {

    private const val CDP_CONSENT = "CDP_CONSENT"

    private fun getSharedPreferences(context: Context) = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

    fun isCdpConsentAccepted(context: Context) = this.getSharedPreferences(context).getBoolean(CDP_CONSENT, false)

    fun acceptCdpConsent(context: Context) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_CONSENT, true).apply()
    }

    fun rejectCdpConsent(context: Context) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_CONSENT, false).apply()
    }
}