package com.teavaro.ecommDemoApp.ui.wishlist

import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseFragment
import com.teavaro.ecommDemoApp.baseClasses.GenericRecyclerViewAdapter
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Item
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentWishlistBinding

class WishlistFragment: BaseFragment<FragmentWishlistBinding>(R.layout.fragment_wishlist, FragmentWishlistBinding::bind) {

    private val wishlistViewModel by lazy { ViewModelProvider(this).get(WishlistViewModel::class.java) }

    override fun initUI() {}
}