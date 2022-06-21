package com.teavaro.teavarodemoapp.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.databinding.FragmentShopBinding
import com.teavaro.teavarodemoapp.ui.cart.CartAdapter

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val shopAdapter = ShopAdapter(requireContext(), listOf(
            Item(1, "puerco", 23.4f),
            Item(2, "picadillo", 20.1f),
            Item(3, "leche", 15.6f),
        ))
        val lvItems = binding.listItems
        lvItems.adapter = shopAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}