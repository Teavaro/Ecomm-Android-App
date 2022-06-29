package com.teavaro.ecommDemoApp.baseClasses.mvvm

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.teavaro.ecommDemoApp.viewBinding

open class BaseActivity<VB: ViewBinding>(viewBindingFactory: (LayoutInflater) -> VB): AppCompatActivity() {
    protected val viewBinding by viewBinding(viewBindingFactory)
}