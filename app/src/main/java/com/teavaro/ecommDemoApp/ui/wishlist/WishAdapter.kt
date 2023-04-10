package com.teavaro.ecommDemoApp.ui.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.core.room.ItemEntity
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.ui.ItemDescriptionDialogFragment
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK
import kotlinx.android.synthetic.main.item_shop.view.btnAddToCart
import kotlinx.android.synthetic.main.item_wish.view.*
import kotlinx.android.synthetic.main.item_wish.view.btnRemove
import kotlinx.android.synthetic.main.item_wish.view.txtPrice
import kotlinx.android.synthetic.main.item_wish.view.txtTitle

class WishAdapter(context: Context,
                  private val listItems: List<ItemEntity>) :
    ArrayAdapter<ItemEntity>(context, 0, listItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_wish,parent, false)

        val item = listItems[position]
        layout.txtTitle.text = item.title
        layout.txtPrice.text = "$${item.price}"

        if(item.isInStock)
            layout.inStock.visibility = ImageView.VISIBLE
        else {
            layout.noStock.visibility = ImageView.VISIBLE
            layout.btnAddToCart.visibility = Button.GONE
        }

        layout.btnRemove.setOnClickListener {
            val events = mapOf(TrackUtils.CLICK to "remove_item_from_wish", "item_id" to item.itemId.toString())
            TrackUtils.events(events)
            Store.removeItemFromWish(item.itemId)
            parent.findNavController().navigate(R.id.navigation_wishlist)
            Toast.makeText(context, "Product removed!", Toast.LENGTH_SHORT).show()
        }

        layout.btnAddToCart.setOnClickListener {
            val events = mapOf(TrackUtils.CLICK to "add_item_to_cart", "item_id" to item.itemId.toString())
            TrackUtils.events(events)
            Store.addItemToCart(item.itemId)
            Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
        }

        layout.txtTitle.setOnClickListener{
            ItemDescriptionDialogFragment.open((context as AppCompatActivity).supportFragmentManager, item, {
                val events = mapOf(TrackUtils.CLICK to "add_item_to_cart", "item_id" to item.itemId.toString())
                TrackUtils.events(events)
                Store.addItemToCart(item.itemId)
            })
        }

        return layout
    }
}