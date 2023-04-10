package com.teavaro.ecommDemoApp.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.room.ItemEntity
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.databinding.FragmentFItemDescriptionBinding
import com.teavaro.ecommDemoApp.viewBinding
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK

class ItemDescriptionDialogFragment(item: ItemEntity) :
    DialogFragment(R.layout.fragment_f_item_description) {

    private val binding by viewBinding(FragmentFItemDescriptionBinding::bind)
    private var addToWishlistAction: (() -> Unit)? = null
    private var addToCartAction: (() -> Unit)? = null
    private var item = item

//    override fun getTheme() = R.style.FullScreenDimmedDialogFragment

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val events = mapOf(TrackUtils.IMPRESSION to "item_view", "item_id" to item.itemId.toString())
        TrackUtils.events(events)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialPresets()
        this.addToWishlistAction?.let {
            binding.btnAddToWish.visibility = Button.VISIBLE
            binding.btnAddToWish.setOnClickListener {
                val events = mapOf(TrackUtils.CLICK to "add_item_to_wish", "item_id" to item.itemId.toString())
                TrackUtils.events(events)
                this.addToWishlistAction?.invoke()
                this.dismiss()
            }
        }

        this.addToCartAction?.let {
            if(item.isInStock == true) {
                binding.btnAddToCart.visibility = Button.VISIBLE
                binding.btnAddToCart.setOnClickListener {
                    val events = mapOf(TrackUtils.CLICK to "add_item_to_cart", "item_id" to item.itemId.toString())
                    TrackUtils.events(events)
                    this.addToCartAction?.invoke()
                    this.dismiss()
                }
            }
        }

        binding.btnClose.setOnClickListener {
            this.dismiss()
        }
    }

    private fun initialPresets() {
        binding.txtTitle.text = item.title
        binding.txtDescription.text = item.desc
        binding.txtPrice.text = "$" + item.price.toString()
        val imgId: Int =
            resources.getIdentifier(item.picture, "drawable", "com.teavaro.ecommDemoApp")
        binding.imgPicture.setImageResource(imgId)
    }

    companion object {

        fun open(
            fm: FragmentManager,
            item: ItemEntity,
            addToCartAction: (() -> Unit)? = null,
            addToWishlistAction: (() -> Unit)? = null
        ) {
            val dialogFragment = ItemDescriptionDialogFragment(item)
            addToWishlistAction?.let { dialogFragment.addToWishlistAction = it }
            addToCartAction?.let { dialogFragment.addToCartAction = it }
            dialogFragment.show(fm, null)
        }
    }
}