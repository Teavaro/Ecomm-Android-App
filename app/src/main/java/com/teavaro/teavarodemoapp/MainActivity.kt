package com.teavaro.teavarodemoapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.teavaro.teavarodemoapp.core.Item
import com.teavaro.teavarodemoapp.core.Store
import com.teavaro.teavarodemoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_wishlist, R.id.navigation_shop
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        when (item.itemId) {
            R.id.menu_login -> {
                navController.navigate(R.id.navigation_home)
                navController.navigate(R.id.navigation_login)
            }
            R.id.menu_logout -> {
                Store.isLogin = false
                val startIntent = Intent(this, MainActivity::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(startIntent)
                Toast.makeText(this, "Logout success!", Toast.LENGTH_SHORT).show()
            }
            else -> navController.navigate(R.id.navigation_home)
        }
        return true
    }
}