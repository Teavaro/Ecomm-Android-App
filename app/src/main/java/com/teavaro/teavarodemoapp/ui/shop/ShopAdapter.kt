package com.teavaro.teavarodemoapp.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.teavaro.teavarodemoapp.R
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.core.Store
import kotlinx.android.synthetic.main.item_shop.view.*

class ShopAdapter(context: Context,
                  private val listItems: List<Item>) :
    ArrayAdapter<Item>(context, 0, listItems) {

    private lateinit var layout: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        layout = LayoutInflater.from(context).inflate(R.layout.item_shop,parent, false)

        val item = listItems[position]
        layout.txtTitle.text = item.title
        layout.txtPrice.text = "$${item.price}"
        layout.imgPicture.setImageResource(R.drawable.prod_snapple)

        layout.btnAddToCart.setOnClickListener {
            Store.addItemToCart(item.id)
        }

        setWishPicture(item)

        layout.btnAddToWish.setOnClickListener {
            if(!item.isWish) {
                Store.addItemToWish(item.id)
                item.isWish = true
            }
            else {
                Store.removeItemFromWish(item.id)
                item.isWish = false
            }
            setWishPicture(item)
        }

        return layout
    }

    fun setWishPicture(item: Item){
        if(item.isWish)
            layout.btnAddToWish.setImageResource(R.drawable.ic_wishlist_red_24dp)
        else
            layout.btnAddToWish.setImageResource(R.drawable.ic_wishlist_black_24dp)
    }
}