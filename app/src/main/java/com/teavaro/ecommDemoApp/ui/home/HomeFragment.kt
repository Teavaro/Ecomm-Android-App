package com.teavaro.ecommDemoApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentHomeBinding
import com.teavaro.ecommDemoApp.ui.shop.ShopAdapter
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK

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

        FunnelConnectSDK.cdp().logEvent("Navigation", "home")

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var list = Store.getItemsOffer()
        val shopAdapter = ShopAdapter(requireContext(), list)
        for (pos in 0..list.lastIndex){
            binding.listItems.addView(shopAdapter.getView(pos, view, container!!))
        }

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