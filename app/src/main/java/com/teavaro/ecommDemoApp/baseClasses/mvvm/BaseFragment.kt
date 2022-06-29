package com.teavaro.ecommDemoApp.baseClasses.mvvm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.teavaro.ecommDemoApp.viewBinding

abstract class BaseFragment<VB : ViewBinding>(layoutId: Int, viewBindingFactory: (View) -> VB): Fragment(layoutId) {

    protected val viewBinding by viewBinding(viewBindingFactory)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initUI()
    }

    protected abstract fun initUI()
}