package com.teavaro.ecommDemoApp.core.utils

import android.content.Context

object SharedPreferenceUtils {

    private const val STUB_MODE = "STUB_MODE"
    private const val IS_LOGIN = "IS_LOGIN"

    private fun getSharedPreferences(context: Context) = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

    fun isStubMode(context: Context) = getSharedPreferences(context).getBoolean(STUB_MODE, false)

    fun setStubMode(context: Context, value: Boolean) {
        getSharedPreferences(context).edit().putBoolean(STUB_MODE, value).apply()
    }

    fun isLogin(context: Context) = getSharedPreferences(context).getBoolean(IS_LOGIN, false)

    fun setLogin(context: Context, value: Boolean) {
        getSharedPreferences(context).edit().putBoolean(IS_LOGIN, value).apply()
    }
}