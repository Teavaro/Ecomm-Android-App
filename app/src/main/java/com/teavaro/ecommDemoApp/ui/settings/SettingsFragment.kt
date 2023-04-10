package com.teavaro.ecommDemoApp.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.utils.HTTPAsyncTask
import com.teavaro.ecommDemoApp.core.utils.SharedPreferenceUtils
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.core.utils.PushNotification
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.databinding.FragmentSettingsBinding
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        TrackUtils.impression("settings_view")

        if(Store.isLogin){
            binding.logOut.visibility = Button.VISIBLE
            binding.logIn.visibility = Button.GONE
        }
        else{
            binding.logOut.visibility = Button.GONE
            binding.logIn.visibility = Button.VISIBLE
        }

        binding.clearData.setOnClickListener{
            clearData()
            Toast.makeText(requireContext(), "Data cleared!", Toast.LENGTH_LONG).show()
        }

        binding.notifications.setOnClickListener {
            root.findNavController().navigate(R.id.navigation_notifications)
        }

        binding.logIn.setOnClickListener {
            root.findNavController().navigate(R.id.navigation_login)
        }

        binding.logOut.setOnClickListener {
            TrackUtils.click("dialog_logout")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout confirmation")
                .setMessage("Do you want to proceed with the logout?")
                .setNegativeButton("Cancel")  {_,_ ->
                    TrackUtils.click("cancel_logout")
                }
                .setPositiveButton("Proceed") { _, _ ->
                    TrackUtils.click("proceed_logout")
                    Store.isLogin = false
                    root.findNavController().navigate(R.id.navigation_settings)
                    Toast.makeText(requireContext(), "Logout success!", Toast.LENGTH_SHORT).show()
                }
                .create().show()
        }

        binding.consentManagement.setOnClickListener {
            Store.showPermissionsDialog(requireContext(), parentFragmentManager)
        }

        binding.stubMode.isChecked = SharedPreferenceUtils.isStubMode(requireContext())
        binding.stubMode.setOnCheckedChangeListener { _, isStub ->
            clearData()
            SharedPreferenceUtils.setStubMode(requireContext(), isStub)
            Store.showPermissionsDialog(requireContext() ,parentFragmentManager)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearData(){
        FunnelConnectSDK.clearData()
        FunnelConnectSDK.clearCookies()
    }
}