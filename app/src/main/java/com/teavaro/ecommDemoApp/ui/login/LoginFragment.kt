package com.teavaro.ecommDemoApp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.swrve.sdk.SwrveSDK
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.core.utils.SharedPreferenceUtils
import com.teavaro.ecommDemoApp.core.utils.StringUtils.stringToSha256String
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.databinding.FragmentLoginBinding
import com.teavaro.ecommDemoApp.ui.notifications.NotificationsViewModel
import com.teavaro.funnelConnect.data.models.FCUser
import com.teavaro.funnelConnect.initializer.FunnelConnectSDK


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

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

        TrackUtils.impression("login_view")

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogin.setOnClickListener {
            TrackUtils.click("login")
            if (!binding.edtEmail.text.isNullOrEmpty() && !binding.edtPassword.text.isNullOrEmpty()) {
                val emailCoded = stringToSha256String(binding.edtEmail.text.toString())
                FunnelConnectSDK.setUser(FCUser("hemail", emailCoded),{
                    Store.infoResponse = it
                })
                SharedPreferenceUtils.setLogin(requireContext(), true)
                root.findNavController().navigate(R.id.navigation_settings)
                SwrveSDK.start(parentFragment?.activity, FunnelConnectSDK.getUMID())
                Toast.makeText(context, "Login success!" + FunnelConnectSDK.getUMID(), Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context, "Need to insert email and password!", Toast.LENGTH_SHORT)
                    .show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}