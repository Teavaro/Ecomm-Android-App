package com.teavaro.ecommDemoApp.ui.shop

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseFragment
import com.teavaro.ecommDemoApp.baseClasses.GenericRecyclerViewAdapter
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Item
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentShopBinding
import com.teavaro.ecommDemoApp.databinding.ItemShopBinding

class ShopFragment : BaseFragment<FragmentShopBinding>(R.layout.fragment_shop, FragmentShopBinding::bind) {

    private val shopViewModel by lazy { ViewModelProvider(this).get(ShopViewModel::class.java) }
    private val adapter by lazy {
        GenericRecyclerViewAdapter.create<Item, ItemShopBinding>(ItemShopBinding::inflate) { context, binding, _, item ->
            binding.txtTitle.text = item.title
            binding.txtPrice.text = "$${item.price}"
            val imgId: Int = context.resources.getIdentifier(item.picture, "drawable", "com.teavaro.teavarodemoapp")
            binding.imgPicture.setImageResource(imgId)
            if(!item.isInStock) {
                binding.btnAddItem.visibility = Button.GONE
                // binding.outOfStock.visibility = TextView.VISIBLE
            }
            binding.btnAddItem.setOnClickListener {
                Store.addItemToCart(item.id)
                Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
            }
            binding.btnAddToWishlist.let { imageView ->
                setWishPicture(imageView, item)
                imageView.setOnClickListener {
                    if(!item.isWish) {
                        Store.addItemToWish(item.id)
                        item.isWish = true
                    }
                    else {
                        Store.removeItemFromWish(item.id)
                        item.isWish = false
                    }
                    setWishPicture(imageView as ImageView, item)
                    Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initUI() {
        viewBinding.listItems.adapter = this.adapter
        this.adapter.listItems = Store.getItems()
        this.adapter.notifyDataSetChanged()
    }

    private fun setWishPicture(imageView: ImageView, item: Item) {
        if(item.isWish)
            imageView.setImageResource(R.drawable.ic_wishlist_black_24dp)
        else
            imageView.setImageResource(R.drawable.ic_wishlist_black_24dp)
    }
}