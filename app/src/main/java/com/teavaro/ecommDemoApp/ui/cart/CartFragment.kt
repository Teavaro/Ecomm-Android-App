package com.teavaro.ecommDemoApp.ui.cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentCartBinding
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK

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

        FunnelConnectSDK.cdp().logEvent("Navigation", "cart")

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
            FunnelConnectSDK.cdp().logEvent("Button", "dialogCheckout")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Checkout confirmation")
                .setMessage("Do you want to proceed with checkout?")
                .setNegativeButton("Cancel")  {_,_ ->
                    FunnelConnectSDK.cdp().logEvent("Button", "cancelCheckout")
                }
                .setPositiveButton("Proceed") { _, _ ->
                    FunnelConnectSDK.cdp().logEvent("Button", "proceedCheckout")
                    Store.removeAllCartItems()
                    root.findNavController().navigate(R.id.navigation_cart)
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                }
                .create().show()
        }

        binding.btnClearCart.setOnClickListener {
            FunnelConnectSDK.cdp().logEvent("Button", "dialogClearCart")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Clear confirmation")
                .setMessage("Do you want to clear the cart?")
                .setNegativeButton("Cancel")  {_,_ ->
                    FunnelConnectSDK.cdp().logEvent("Button", "cancelClearCart")
                }
                .setPositiveButton("Proceed") { _, _ ->
                    FunnelConnectSDK.cdp().logEvent("Button", "proceedClearCart")
                    val acId = Store.addAbandonedCart(Store.getItemsCart())
                    FunnelConnectSDK.cdp().logEvent("abandoned_cart_id", acId.toString())
                    Store.removeAllCartItems()
                    root.findNavController().navigate(R.id.navigation_cart)
                    Toast.makeText(context, "Cart cleared!", Toast.LENGTH_SHORT).show()
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