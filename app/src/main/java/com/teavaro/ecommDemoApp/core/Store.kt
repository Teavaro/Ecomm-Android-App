package com.teavaro.ecommDemoApp.core

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.teavaro.ecommDemoApp.ui.PermissionConsentDialogFragment
import com.teavaro.funnelConnect.core.initializer.FunnelConnectSDK
import com.teavaro.funnelConnect.utils.platformTypes.permissionsMap.PermissionsMap

object Store {
    private var listItems: ArrayList<Item> = ArrayList()
    var section = ""
    var isLogin = false
    var infoResponse: String = "{}"
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

    fun getBanner(): String{
        var text = ""
        var gson = Gson()
        var obj: InfoResponse = gson.fromJson(infoResponse, InfoResponse::class.java)
        obj?.let { ob ->
            ob.attributes?.let { attr ->
                attr.forEach {
                    text += "&amp;" + it.key + "=" + it.value
                }
            }
        }
        Log.d("iran:infoResponse", infoResponse)
        Log.d("iran:attr", text)
        return """
           <!DOCTYPE html>
           <html>
           <body>
            <div class="celtra-ad-v3">
                <img src="data:image/png,celtra" style="display: none" onerror="
                    (function(img) {
                        var params = {'clickUrl':'','widthBreakpoint':'','expandDirection':'undefined','preferredClickThroughWindow':'new','clickEvent':'advertiser','externalAdServer':'Custom','tagVersion':'html-standard-7'};
                        var req = document.createElement('script');
                        req.id = params.scriptId = 'celtra-script-' + (window.celtraScriptIndex = (window.celtraScriptIndex||0)+1);
                        params.clientTimestamp = new Date/1000;
                        params.clientTimeZoneOffsetInMinutes = new Date().getTimezoneOffset();
                        params.hostPageLoadId=window.celtraHostPageLoadId=window.celtraHostPageLoadId||(Math.random()+'').slice(2);
                        var qs = '';
                        for (var k in params) {
                            qs += '&amp;' + encodeURIComponent(k) + '=' + encodeURIComponent(params[k]);
                        }
                        var src = 'https://ads.celtra.com/67444e1d/web.js?' + qs + '$text';
                        req.src = src;
                        img.parentNode.insertBefore(req, img.nextSibling);
                    })(this);
                "/>
            </div>
           </body>
           </html>
        """.trimIndent()
    }
}