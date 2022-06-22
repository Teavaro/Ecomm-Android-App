package com.teavaro.teavarodemoapp.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.core.Store
import com.teavaro.teavarodemoapp.databinding.FragmentCartBinding

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

        val cartAdapter = CartAdapter(requireContext(), Store.getItemsCart())
        binding.listItems.adapter = cartAdapter

        binding.txtTotal.text = "$${Store.getTotalPriceCart()} in total"

        if(cartAdapter.count == 0)
            binding.txtEmpty.visibility = LinearLayout.VISIBLE
        else
            binding.layTotal.visibility = LinearLayout.VISIBLE

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}