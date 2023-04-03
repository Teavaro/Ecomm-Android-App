package com.teavaro.ecommDemoApp.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.core.room.ItemEntity
import com.teavaro.ecommDemoApp.ui.ItemDescriptionDialogFragment
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_cart.view.txtPrice
import kotlinx.android.synthetic.main.item_cart.view.txtTitle

class CartAdapter(context: Context,
                  private val listItems: List<ItemEntity>) :
    ArrayAdapter<ItemEntity>(context, 0, listItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_cart,parent, false)

        val item = listItems[position]
        val subTotal: Float = item.price!! * item.countInCart
        layout.txtTitle.text = item.title
        layout.txtPrice.text = item.price.toString()
        layout.txtCount.text = item.countInCart.toString()
        layout.txtSubTotal.text = "$$subTotal / piece"

        layout.btnRemove.setOnClickListener {
            FunnelConnectSDK.cdp().logEvent("Button", "removeFromCart")
            Store.removeItemFromCart(item.itemId)
            parent.findNavController().navigate(R.id.navigation_cart)
            Toast.makeText(context, "Product removed!", Toast.LENGTH_SHORT).show()
        }

        layout.txtTitle.setOnClickListener{
            ItemDescriptionDialogFragment.open((context as AppCompatActivity).supportFragmentManager, item)
        }

        return layout
    }
}