package com.teavaro.teavarodemoapp.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.teavaro.teavarodemoapp.R
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.core.Store
import kotlinx.android.synthetic.main.item_shop.view.*

class ShopAdapter(context: Context,
                  private val listItems: List<Item>) :
    ArrayAdapter<Item>(context, 0, listItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_shop,parent, false)

        val item = listItems[position]
        layout.txtTitle.text = item.title
        layout.txtPrice.text = item.price.toString()
        layout.imgPicture.setImageResource(R.drawable.prod_snapple)

        layout.btnAddToCart.setOnClickListener {
            Store.addItemToCart(item.id)
        }

        if(item.isWish)
            layout.btnAddToWish.setImageResource(R.drawable.ic_wishlist_red_24dp)
        else
            layout.btnAddToWish.setImageResource(R.drawable.ic_wishlist_black_24dp)

        layout.btnAddToWish.setOnClickListener {
            Store.addItemToWish(item.id)
        }

        return layout
    }
}