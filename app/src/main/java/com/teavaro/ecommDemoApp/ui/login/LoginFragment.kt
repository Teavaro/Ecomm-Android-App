package com.teavaro.ecommDemoApp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.LogInMenu
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentLoginBinding


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
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogin.setOnClickListener {
            if (!binding.edtEmail.text.isNullOrEmpty() && !binding.edtPassword.text.isNullOrEmpty()) {
                Store.isLogin = true
                Toast.makeText(context, "Login success!", Toast.LENGTH_SHORT).show()
                root.findNavController().navigate(R.id.navigation_home)
                LogInMenu.menu.getItem(1).title = "Log out"
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