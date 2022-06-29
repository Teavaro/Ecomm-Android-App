package com.teavaro.ecommDemoApp.baseClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.teavaro.ecommDemoApp.viewBinding

abstract class GenericRecyclerViewAdapter<T, VB: ViewBinding, VH: GenericViewHolder<T, VB>>(private val viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> VB): RecyclerView.Adapter<VH>() {

    var listItems: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VH {
        val layoutBinding = viewGroup.viewBinding(viewBindingFactory)
        return this.createViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        viewHolder.bindViewHolder(position, viewHolder.itemView.context, this.getItem(position))
    }

    override fun getItemCount() = this.listItems.size

    protected fun getItem(position: Int): T = this.listItems[position]

    abstract fun createViewHolder(viewBinding: VB): VH

    companion object {

        fun <T, VB: ViewBinding> create(viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> VB, viewHolderFactory: (context: Context, viewBinding: VB, position: Int, element: T) -> Unit): GenericRecyclerViewAdapter<T, VB, GenericViewHolder<T, VB>> {
            return object : GenericRecyclerViewAdapter<T, VB, GenericViewHolder<T, VB>>(viewBindingFactory) {
                override fun createViewHolder(viewBinding: VB) = object: GenericViewHolder<T, VB>(viewBinding) {
                    override fun bindViewHolder(position: Int, context: Context, element: T?) {
                        element?.let {
                            viewHolderFactory(context, viewBinding, position, element)
                        }
                    }
                }
            }
        }
    }
}