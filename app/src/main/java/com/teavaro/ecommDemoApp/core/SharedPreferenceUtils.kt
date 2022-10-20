package com.teavaro.ecommDemoApp.core

import android.content.Context

object SharedPreferenceUtils {

    private const val CDP_CONSENT = "CDP_CONSENT"
    private const val CDP_OM = "CDP_OM"
    private const val CDP_OPT = "CDP_OPT"
    private const val CDP_NBA = "CDP_NBA"

    private fun getSharedPreferences(context: Context) = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

    fun isCdpConsentAccepted(context: Context) = this.getSharedPreferences(context).getBoolean(CDP_CONSENT, false)

    fun acceptCdpConsent(context: Context) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_CONSENT, true).apply()
    }

    fun rejectCdpConsent(context: Context) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_CONSENT, false).apply()
    }

    fun isCdpOm(context: Context) = this.getSharedPreferences(context).getBoolean(CDP_OM, false)

    fun setCdpOm(context: Context, value: Boolean) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_OM, value).apply()
    }

    fun isCdpOpt(context: Context) = this.getSharedPreferences(context).getBoolean(CDP_OPT, false)

    fun setCdpOpt(context: Context, value: Boolean) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_OPT, value).apply()
    }

    fun isCdpNba(context: Context) = this.getSharedPreferences(context).getBoolean(CDP_NBA, false)

    fun setCdpNba(context: Context, value: Boolean) {
        this.getSharedPreferences(context).edit().putBoolean(CDP_NBA, value).apply()
    }
}