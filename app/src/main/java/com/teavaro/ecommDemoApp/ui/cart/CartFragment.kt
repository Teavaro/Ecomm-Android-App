package com.teavaro.ecommDemoApp.ui.cart

import android.app.AlertDialog
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentCartBinding
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseFragment
import com.teavaro.ecommDemoApp.baseClasses.GenericRecyclerViewAdapter
import com.teavaro.ecommDemoApp.core.Item
import com.teavaro.ecommDemoApp.databinding.ItemShopBinding

class CartFragment: BaseFragment<FragmentCartBinding>(R.layout.fragment_cart, FragmentCartBinding::bind) {

    private val cartViewModel by lazy { ViewModelProvider(this).get(CartViewModel::class.java) }
    private val adapter by lazy {
        GenericRecyclerViewAdapter.create<String, ItemShopBinding>(ItemShopBinding::inflate) { context, binding, _, item ->
            binding.txtTitle.text = item
        }
    }

    override fun initUI() {
        viewBinding.listItems.adapter = this.adapter
        this.adapter.listItems = mutableListOf("Puerco", "Pollo", "Picadillo")
        this.adapter.notifyDataSetChanged()
        viewBinding.txtTotal.text = "$${Store.getTotalPriceCart()} in total"
        if(adapter.listItems.isEmpty())
            viewBinding.txtEmpty.visibility = LinearLayout.VISIBLE
        else
            viewBinding.layTotal.visibility = LinearLayout.VISIBLE
        viewBinding.btnCheckout.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Checkout confirmation")
                .setMessage("Do you want to proceed with checkout?")
                .setNegativeButton("Cancel")  {_,_ ->

                }
                .setPositiveButton("Proceed") { _, _ ->
                    Store.removeAllCartItems()
                    this.findNavController().navigate(R.id.navigation_cart)
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                }
                .create().show()
        }
    }
}