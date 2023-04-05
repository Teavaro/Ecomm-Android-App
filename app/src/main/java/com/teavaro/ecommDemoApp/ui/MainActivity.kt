package com.teavaro.ecommDemoApp.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.swrve.sdk.SwrveSDK
import com.swrve.sdk.geo.SwrveGeoSDK
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.baseClasses.mvvm.BaseActivity
import com.teavaro.ecommDemoApp.core.*
import com.teavaro.ecommDemoApp.core.room.AppDb
import com.teavaro.ecommDemoApp.core.utils.SharedPreferenceUtils
import com.teavaro.ecommDemoApp.core.utils.TrackUtils
import com.teavaro.ecommDemoApp.databinding.ActivityMainBinding
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val navController by lazy { this.findNavController(R.id.nav_host_fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        Log.d("TrackUtils","initialized")
        TrackUtils.lifeCycle(lifecycle)

        var db = Room.databaseBuilder(applicationContext, AppDb::class.java, "TeavaroEcommDB")
//            .fallbackToDestructiveMigration()
            .build()

        val navView: BottomNavigationView = viewBinding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_cart,
                R.id.navigation_wishlist,
                R.id.navigation_shop,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(this.navController, appBarConfiguration)
        navView.setupWithNavController(this.navController)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.logo2)
        if (resources.getString(R.string.mode) == "Day") {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }

        Store.initializeData(db){
            this@MainActivity.runOnUiThread {
                navController.navigate(it)
            }
        }

        FunnelConnectSDK.onInitialize({
            if (FunnelConnectSDK.trustPid().isConsentAccepted()) {
                val isStub = SharedPreferenceUtils.isStubMode(this)
                FunnelConnectSDK.trustPid().startService(isStub)
            }
            FunnelConnectSDK.cdp()
                .startService(null, Store.notificationName, Store.notificationVersion, {
                    Store.infoResponse = it
                    if (FunnelConnectSDK.cdp().getPermissions().isEmpty())
                        Store.showPermissionsDialog(this, supportFragmentManager)
                    SwrveSDK.start(this, FunnelConnectSDK.cdp().getUmid())
                    SwrveGeoSDK.start(this)
                },
                    {
                    })
        }) {
        }

        handleIntent(intent)
    }

    fun setOverflowButtonColor(toolbar: Toolbar, color: Int) {
        var drawable: Drawable? = toolbar.overflowIcon
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawable.mutate(), color)
            toolbar.overflowIcon = drawable
        }
    }

    private fun handleIntent(intent: Intent?) {
        val appLinkAction: String? = intent?.action
        val appLinkData: Uri? = intent?.data
        showDeepLinkOffer(appLinkAction, appLinkData)
    }

    private fun showDeepLinkOffer(appLinkAction: String?, appLinkData: Uri?) {
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            Store.handleDeepLink(appLinkData, supportFragmentManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.login_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            else -> navController.navigate(R.id.navigation_settings)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        when (Store.section) {
            "store" -> {
                Log.d("iraniran", "section:${Store.section}")
                navController.navigate(R.id.navigation_shop)
                Store.section = ""
            }
        }
    }
}