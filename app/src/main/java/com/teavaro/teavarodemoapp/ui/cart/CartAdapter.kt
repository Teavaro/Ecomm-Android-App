package com.teavaro.teavarodemoapp.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.teavaro.teavarodemoapp.R
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.core.Store
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_cart.view.txtPrice
import kotlinx.android.synthetic.main.item_cart.view.txtTitle

class CartAdapter(context: Context,
                  private val listItems: List<Item>) :
    ArrayAdapter<Item>(context, 0, listItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_cart,parent, false)

        val item = listItems[position]
        val subTotal: Float = item.price * item.countOnCart
        layout.txtTitle.text = item.title
        layout.txtPrice.text = item.price.toString()
        layout.txtCount.text = item.countOnCart.toString()
        layout.txtSubTotal.text = subTotal.toString()

        layout.btnRemove.setOnClickListener {
            Store.removeItemFromCart(item.id)
            parent.findNavController().navigate(R.id.navigation_cart)
        }

        return layout
    }
}