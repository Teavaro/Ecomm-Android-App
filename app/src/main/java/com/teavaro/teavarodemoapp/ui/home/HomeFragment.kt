package com.teavaro.teavarodemoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.teavaro.teavarodemoapp.R
import com.teavaro.teavarodemoapp.core.Store
import com.teavaro.teavarodemoapp.databinding.FragmentHomeBinding
import com.teavaro.teavarodemoapp.ui.shop.ShopAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val shopAdapter = ShopAdapter(requireContext(), Store.getItemsOffer())
        binding.listItems.adapter = shopAdapter

        binding.btnExplore.setOnClickListener {
            root.findNavController().navigate(R.id.navigation_shop)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}