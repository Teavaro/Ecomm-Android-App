package com.teavaro.ecommDemoApp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.databinding.FragmentHomeBinding
import com.teavaro.ecommDemoApp.ui.shop.ShopAdapter


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

        TrackUtils.impression("home_view")
        Store.section = "home"
        var list = Store.listOffers
        val shopAdapter = ShopAdapter(requireContext(), list)
        for (pos in 0..list.lastIndex) {
            binding.listItems.addView(shopAdapter.getView(pos, view, container!!))
        }

        binding.btnExplore.setOnClickListener {
            TrackUtils.click("explore")
            Store.navigateAction?.invoke(R.id.navigation_shop)
        }
        if (Store.infoResponse != null)
            loadAd()
        Store.refreshCeltraAd = {
            if (Store.section == "home")
                loadAd()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Instantiate the interface and set the context  */
    class WebAppInterface(
        private var context: Context,
        private var supportFragmentManager: FragmentManager
    ) {
        /** Show a toast from the web page  */
        @JavascriptInterface
        fun postMessage(data: String) {
            Store.processCelraAction(context, data, supportFragmentManager)
        }
    }

    private fun loadAd() {
//        if(Store.webView == null) {
        binding.webView.removeAllViews()
        var webView = WebView(requireContext())
        val html = Store.getBanner()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                Store.webView = webView
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.addJavascriptInterface(
            WebAppInterface(
                requireContext(),
                parentFragmentManager
            ), "Android"
        )
        webView.loadDataWithBaseURL(
            "http://www.example.com/",
            html,
            "text/html",
            "UTF-8",
            null
        )
        binding.webView.addView(webView)
//        }
//        else{
//            Store.webView?.let {
//                binding.webView.addView(it)
//            }
//        }
    }

    override fun onPause() {
        super.onPause()
        binding.webView.removeAllViews()
    }
}


