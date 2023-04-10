package com.teavaro.ecommDemoApp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teavaro.ecommDemoApp.core.utils.PushNotification
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.databinding.FragmentNotificationsBinding
import com.teavaro.ecommDemoApp.ui.login.NotificationsViewModel
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        TrackUtils.impression("notifications_view")

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.crinklys.setOnClickListener {
            FunnelConnectSDK.cdp().getUmid()?.let {
                PushNotification.sendCrilklys(it){
                    Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.watermelon.setOnClickListener {
            FunnelConnectSDK.cdp().getUmid()?.let {
                PushNotification.sendWatermelon(it){
                    Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.paprika.setOnClickListener {
            FunnelConnectSDK.cdp().getUmid()?.let {
                PushNotification.sendPaprika(it){
                    Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.shop.setOnClickListener {
            FunnelConnectSDK.cdp().getUmid()?.let {
                PushNotification.sendShop(it){
                    Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.abandonedCart.setOnClickListener {
            FunnelConnectSDK.cdp().getUmid()?.let {
                PushNotification.sendAbandonedCart(it){
                    Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_LONG).show()
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}