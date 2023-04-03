package com.teavaro.ecommDemoApp.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.core.room.ItemEntity
import com.teavaro.ecommDemoApp.ui.ItemDescriptionDialogFragment
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK
import kotlinx.android.synthetic.main.item_shop.view.*

class ShopAdapter(context: Context,
                  private val listItems: List<ItemEntity>) :
    ArrayAdapter<ItemEntity>(context, 0, listItems) {

    private lateinit var layout: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        layout = LayoutInflater.from(context).inflate(R.layout.item_shop,parent, false)

        val item = listItems[position]
        layout.txtTitle.text = item.title
        layout.txtPrice.text = "$${item.price}"
        val imgId: Int = parent.resources.getIdentifier(item.picture, "drawable", "com.teavaro.ecommDemoApp")
        layout.imgPicture.setImageResource(imgId)

        if(item.isInStock == false) {
            layout.btnAddToCart.visibility = Button.GONE
            layout.outOfStock.visibility = TextView.VISIBLE
        }


        layout.btnAddToCart.setOnClickListener {
            FunnelConnectSDK.cdp().logEvent("Button", "addToCart")
            Store.addItemToCart(item.itemId)
            Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
        }

        layout.btnAddToWish.let { imageView ->
            setWishPicture(imageView, item)
            imageView.setOnClickListener {
                if(item.isInWish == false) {
                    FunnelConnectSDK.cdp().logEvent("Button", "addToWish")
                    Store.addItemToWish(item.itemId)
                    item.isInWish = true
                    Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
                }
                else {
                    FunnelConnectSDK.cdp().logEvent("Button", "removeFromWish")
                    Store.removeItemFromWish(item.itemId)
                    item.isInWish = false
                    Toast.makeText(context, "Product removed!", Toast.LENGTH_SHORT).show()
                }
                setWishPicture(imageView as ImageView, item)
            }
        }

        layout.imgPicture.setOnClickListener{
            if(item.isInWish == true) {
                ItemDescriptionDialogFragment.open(
                    (context as AppCompatActivity).supportFragmentManager,
                    item,
                    {
                        Store.addItemToCart(item.itemId)
                    })
            }
            else{
                ItemDescriptionDialogFragment.open((context as AppCompatActivity).supportFragmentManager, item, {
                    Store.addItemToCart(item.itemId)
                },{
                    Store.addItemToWish(item.itemId)
                })
            }
        }

        return layout
    }

    private fun setWishPicture(imageView: ImageView, item: ItemEntity){
        if(item.isInWish == true)
            imageView.setImageResource(R.drawable.ic_wishlist_red_24dp)
        else
            imageView.setImageResource(R.drawable.ic_wishlist_black_24dp)
    }
}