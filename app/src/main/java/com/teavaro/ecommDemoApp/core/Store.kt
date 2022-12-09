package com.teavaro.ecommDemoApp.core

import android.app.NotificationManager
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.teavaro.ecommDemoApp.ui.PermissionConsentDialogFragment
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK
import com.teavaro.funnelConnect.utils.platformTypes.permissionsMap.PermissionsMap

object Store {

    private var listItems: ArrayList<Item> = ArrayList()
    var isLogin = false
    val notificationName = "APP_CS"
    val notificationVersion = 4
    var description = "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don’t look even slightly believable. If you are going to use a passage of Lorem Ipsum."

    init {
        listItems.add(Item(0, "Jacob’s Baked Crinklys Cheese", description,60.00f, "crinklys", true))
        listItems.add(Item(1, "Pork Cocktail Sausages, Pack", description, 54.00f, "pork", true, false))
        listItems.add(Item(2, "Broccoli and Cauliflower Mix", description, 6.00f, "cauliflower"))
        listItems.add(Item(3, "Morrisons Creamed Rice Pudding", description, 44.00f, "paprika"))
        listItems.add(Item(4, "Fresh For The Bold Ground Amazon", description, 12.00f, "burst"))
        listItems.add(Item(5, "Frito-Lay Doritos & Cheetos Mix", description, 20.00f, "watermelon"))
        listItems.add(Item(6, "Green Mountain Coffee Roast", description, 20.00f, "grapes"))
        listItems.add(Item(7, "Nature’s Bakery Whole Wheat Bars", description, 50.00f, "mixed"))
    }

    fun addItemToCart(id: Int) {
        listItems[id].countOnCart += 1
    }

    fun removeItemFromCart(id: Int) {
        listItems[id].countOnCart = 0
    }

    fun addItemToWish(id: Int) {
        listItems[id].isWish = true
    }

    fun removeItemFromWish(id: Int) {
        listItems[id].isWish = false
    }

    fun getItems(): ArrayList<Item> {
        return listItems
    }

    fun getItemsCart(): ArrayList<Item> {
        val listCart = ArrayList<Item>()
        for (item in listItems) {
            if (item.countOnCart > 0)
                listCart.add(item)
        }
        return listCart
    }

    fun getItemsWish(): ArrayList<Item> {
        val listWish = ArrayList<Item>()
        for (item in listItems) {
            if (item.isWish)
                listWish.add(item)
        }
        return listWish
    }

    fun getTotalPriceCart(): Float {
        var total = 0f
        for (item in listItems) {
            total += item.price * item.countOnCart
        }
        return total
    }

    fun getItemsOffer(): ArrayList<Item> {
        val listOffer = ArrayList<Item>()
        for (item in listItems) {
            if (item.isOffer)
                listOffer.add(item)
        }
        return listOffer
    }

    fun removeAllCartItems() {
        for (item in listItems) {
            item.countOnCart = 0
        }
    }

    fun showPermissionsDialog(context: Context, supportFragmentManager: FragmentManager) {
        PermissionConsentDialogFragment.open(
            supportFragmentManager,
            { omPermissionAccepted, optPermissionAccepted, nbaPermissionAccepted ->
                val permissions = PermissionsMap()
                permissions.addPermission("CS-TMI",omPermissionAccepted)
                permissions.addPermission("CS-OPT",optPermissionAccepted)
                permissions.addPermission("CS-NBA",nbaPermissionAccepted)
                FunnelConnectSDK.cdp().updatePermissions(permissions, notificationName,notificationVersion)
                if(nbaPermissionAccepted) {
                    FunnelConnectSDK.trustPid().acceptConsent()
                    val isStub = SharedPreferenceUtils.isStubMode(context)
                    FunnelConnectSDK.trustPid().startService(isStub)
                }
                else
                    FunnelConnectSDK.trustPid().rejectConsent()
            },
            {
                FunnelConnectSDK.trustPid().rejectConsent()
                val permissions = PermissionsMap()
                permissions.addPermission("CS-TMI",false)
                permissions.addPermission("CS-OPT",false)
                permissions.addPermission("CS-NBA",false)
                FunnelConnectSDK.cdp().updatePermissions(permissions, notificationName,notificationVersion)
            })
    }
}