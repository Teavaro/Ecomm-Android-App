package com.teavaro.ecommDemoApp.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseActivity
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.ecommDemoApp.databinding.ActivityMainBinding

class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val navController by lazy { this.findNavController(R.id.nav_host_fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val navView: BottomNavigationView = viewBinding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_cart,
            R.id.navigation_wishlist,
            R.id.navigation_shop
        )
        )
        setupActionBarWithNavController(this.navController, appBarConfiguration)
        navView.setupWithNavController(this.navController)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_logo)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.login_menu, menu)
        val idLogin = menu.getItem(0).itemId
        val idLogout = menu.getItem(1).itemId
        if(Store.isLogin)
            menu.removeItem(idLogin)
        else
            menu.removeItem(idLogout)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_login -> {
                this.navController.navigate(R.id.navigation_home)
                this.navController.navigate(R.id.navigation_login)
            }
            R.id.menu_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout confirmation")
                    .setMessage("Do you want to proceed with the logout?")
                    .setNegativeButton("Cancel")  {_,_ ->

                    }
                    .setPositiveButton("Proceed") { _, _ ->
                        Store.isLogin = false
                        val startIntent = Intent(this, MainActivity::class.java)
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(startIntent)
                        Toast.makeText(this, "Logout success!", Toast.LENGTH_SHORT).show()
                    }
                    .create().show()
            }
            else -> navController.navigate(R.id.navigation_home)
        }
        return true
    }
}