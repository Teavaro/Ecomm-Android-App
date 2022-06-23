package com.teavaro.teavarodemoapp.ui.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.teavaro.teavarodemoapp.R
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.core.Store
import kotlinx.android.synthetic.main.item_shop.view.*
import kotlinx.android.synthetic.main.item_wish.view.btnRemove
import kotlinx.android.synthetic.main.item_wish.view.txtPrice
import kotlinx.android.synthetic.main.item_wish.view.txtTitle

class WishAdapter(context: Context,
                  private val listItems: List<Item>) :
    ArrayAdapter<Item>(context, 0, listItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_wish,parent, false)

        val item = listItems[position]
        layout.txtTitle.text = item.title
        layout.txtPrice.text = "$${item.price}"

        layout.btnRemove.setOnClickListener {
            Store.removeItemFromWish(item.id)
            parent.findNavController().navigate(R.id.navigation_wishlist)
            Toast.makeText(context, "An item was removed from the wishlist.", Toast.LENGTH_SHORT).show()
        }

        layout.btnAddToCart.setOnClickListener {
            Store.addItemToCart(item.id)
            Toast.makeText(context, "An item was added to the cart.", Toast.LENGTH_SHORT).show()
        }

        return layout
    }
}