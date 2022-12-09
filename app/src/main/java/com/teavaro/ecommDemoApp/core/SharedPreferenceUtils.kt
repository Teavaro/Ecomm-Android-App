package com.teavaro.ecommDemoApp.core

import android.content.Context

object SharedPreferenceUtils {

    private const val STUB_MODE = "STUB_MODE"

    private fun getSharedPreferences(context: Context) = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

    fun isStubMode(context: Context) = this.getSharedPreferences(context).getBoolean(STUB_MODE, false)

    fun setStubMode(context: Context, value: Boolean) {
        this.getSharedPreferences(context).edit().putBoolean(STUB_MODE, value).apply()
    }
}