package com.teavaro.ecommDemoApp.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseFragment
import com.teavaro.ecommDemoApp.ui.MainActivity
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.FragmentLoginBinding

class LoginFragment: BaseFragment<FragmentLoginBinding>(R.layout.fragment_login, FragmentLoginBinding::bind) {

    private val loginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    override fun initUI() {
//        viewBinding.btnLogin.setOnClickListener {
//            if (!viewBinding.edtEmail.text.isNullOrEmpty() && !viewBinding.edtPassword.text.isNullOrEmpty()) {
//                Store.isLogin = true
//                val startIntent = Intent(context, MainActivity::class.java)
//                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                startActivity(startIntent)
//                Toast.makeText(context, "Login success!", Toast.LENGTH_SHORT).show()
//            } else
//                Toast.makeText(context, "Need to insert email and password!", Toast.LENGTH_SHORT).show()
//        }
    }
}