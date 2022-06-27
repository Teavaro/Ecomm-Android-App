package com.teavaro.teavarodemoapp.ui.cart

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.teavaro.teavarodemoapp.R
import com.teavaro.teavarodemoapp.core.Store
import com.teavaro.teavarodemoapp.databinding.FragmentCartBinding
import com.teavaro.teavarodemoapp.ui.shop.ShopAdapter

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var list = Store.getItemsCart()
        val cartAdapter = CartAdapter(requireContext(), list)
        for (pos in 0..list.lastIndex){
            container?.let {
                binding.listItems.addView(cartAdapter.getView(pos, view, it), 0)
            }
        }

        binding.txtTotal.text = "$${Store.getTotalPriceCart()} in total"

        if(cartAdapter.count == 0)
            binding.txtEmpty.visibility = LinearLayout.VISIBLE
        else
            binding.layTotal.visibility = LinearLayout.VISIBLE

        binding.btnCheckout.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Checkout confirmation")
                .setMessage("Do you want to proceed with checkout?")
                .setNegativeButton("Cancel")  {_,_ ->

                }
                .setPositiveButton("Proceed") { _, _ ->
                    Store.removeAllCartItems()
                    root.findNavController().navigate(R.id.navigation_home)
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                }
                .create().show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}