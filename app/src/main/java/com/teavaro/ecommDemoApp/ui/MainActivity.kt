package com.teavaro.ecommDemoApp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.swrve.sdk.SwrveInitMode
import com.swrve.sdk.SwrveSDK
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseActivity
import com.teavaro.ecommDemoApp.core.LogInMenu
import com.teavaro.ecommDemoApp.core.SharedPreferenceUtils
import com.teavaro.ecommDemoApp.databinding.ActivityMainBinding
import com.teavaro.ecommDemoApp.core.Store
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK

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

        if(!SharedPreferenceUtils.isCdpConsentAccepted(this))
            showPermissionsDialog()

        SwrveSDK.start(this, FunnelConnectSDK.cdp().getUmid())
    }

//    override fun onStart() {
//        super.onStart()
//        FunnelConnectSDK.trustPid().acceptConsent()
//        FunnelConnectSDK.trustPid().startService(true)
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.login_menu, menu)
        LogInMenu.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_login -> {
                when(LogInMenu.menu.getItem(1).title){
                    "Log in" -> {
                        this.navController.navigate(R.id.navigation_login)
                    }
                    "Log out" -> {
                        FunnelConnectSDK.cdp().logEvent("Button", "dialogLogout")
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Logout confirmation")
                            .setMessage("Do you want to proceed with the logout?")
                            .setNegativeButton("Cancel")  {_,_ ->
                                FunnelConnectSDK.cdp().logEvent("Button", "cancelLogout")
                            }
                            .setPositiveButton("Proceed") { _, _ ->
                                FunnelConnectSDK.cdp().logEvent("Button", "proceedLogout")
                                LogInMenu.menu.getItem(1).title = "Log in"
                                Store.isLogin = false
                                navController.navigate(R.id.navigation_home)
                                Toast.makeText(this, "Logout success!", Toast.LENGTH_SHORT).show()
                            }
                            .create().show()
                    }
                }
            }
            R.id.menu_permissions -> {
                showPermissionsDialog()
            }
            else -> navController.navigate(R.id.navigation_home)
        }
        return true
    }

    private fun showPermissionsDialog() {
        PermissionConsentDialogFragment.open(
            supportFragmentManager,
            { omPermissionAccepted, optPermissionAccepted, nbaPermissionAccepted ->
                SharedPreferenceUtils.acceptCdpConsent(this)
                FunnelConnectSDK.cdp().updatePermissions(
                    omPermissionAccepted,
                    optPermissionAccepted,
                    nbaPermissionAccepted
                )

                if(nbaPermissionAccepted) {
//                    if(!FunnelConnectSDK.trustPid().isConsentAccepted()) {
                        FunnelConnectSDK.trustPid().acceptConsent()
                        FunnelConnectSDK.trustPid().startService(true)
//                    }
                }
                else
                    FunnelConnectSDK.trustPid().rejectConsent()
            },
            {
                SharedPreferenceUtils.rejectCdpConsent(this)
                FunnelConnectSDK.trustPid().rejectConsent()
                FunnelConnectSDK.cdp().updatePermissions(false, false, false)
            })
    }
}