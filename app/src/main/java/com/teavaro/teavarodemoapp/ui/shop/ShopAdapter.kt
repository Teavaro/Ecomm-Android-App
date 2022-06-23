package com.teavaro.teavarodemoapp.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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

        if(!item.isInStock)
            layout.btnAddToCart.visibility = Button.GONE

        layout.btnAddToCart.setOnClickListener {
            Store.addItemToCart(item.id)
            Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
        }

        layout.btnAddToWish.let { imageView ->
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

        return layout
    }

    private fun setWishPicture(imageView: ImageView, item: Item){
        if(item.isWish)
            imageView.setImageResource(R.drawable.ic_wishlist_red_24dp)
        else
            imageView.setImageResource(R.drawable.ic_wishlist_black_24dp)
    }
}