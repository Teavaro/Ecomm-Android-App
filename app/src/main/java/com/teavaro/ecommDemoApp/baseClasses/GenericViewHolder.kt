package com.teavaro.ecommDemoApp.baseClasses

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class GenericViewHolder<T, VB: ViewBinding>(protected val viewBinding: VB): RecyclerView.ViewHolder(viewBinding.root) {
    abstract fun bindViewHolder(position: Int, context: Context, element: T?)
}