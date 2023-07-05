package com.teavaro.ecommDemoApp.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.teavaro.ecommDemoApp.R
import com.teavaro.ecommDemoApp.core.dataClases.InfoResponse
import com.teavaro.ecommDemoApp.core.room.ACEntity
import com.teavaro.ecommDemoApp.core.room.AppDb
import com.teavaro.ecommDemoApp.core.room.ItemEntity
import com.teavaro.ecommDemoApp.core.utils.SharedPreferenceUtils
import com.teavaro.ecommDemoApp.ui.AbandonedCartDialogFragment
import com.teavaro.ecommDemoApp.ui.ItemDescriptionDialogFragment
import com.teavaro.ecommDemoApp.ui.PermissionConsentDialogFragment
import com.teavaro.funnelConnect.initializer.FunnelConnectSDK
import com.teavaro.funnelConnect.utils.platformTypes.permissionsMap.PermissionsMap
import com.teavaro.initializer.UTIQ
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
object Store {
    private var db: AppDb? = null
    var listItems: ArrayList<ItemEntity> = ArrayList()
    var listOffers: ArrayList<ItemEntity> = ArrayList()
    var listCart: ArrayList<ItemEntity> = ArrayList()
    var listWish: ArrayList<ItemEntity> = ArrayList()
    var listAc: ArrayList<ACEntity> = ArrayList()
    var section = "none"
    var webView: WebView? = null
    var navigateAction: ((Int) -> Unit)? = null
    var infoResponse: String? = null
    val notificationName = "MAIN_CS"
    val notificationVersion = 1
    var atid = ""
    var mtid = ""
    var itemId = ""
    var description =
        "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don’t look even slightly believable. If you are going to use a passage of Lorem Ipsum."

    init {

    }

    fun addItemToCart(id: Int) {
        listItems[id].countInCart += 1
        updateItemDB(listItems[id])
        listCart = getItemsCart()
    }

    fun removeItemFromCart(id: Int) {
        listItems[id].countInCart = 0
        updateItemDB(listItems[id])
        listCart = getItemsCart()
    }

    fun updateItemDB(item: ItemEntity) {
        db?.let {
            Thread {
                it.itemDao().update(item)
            }.start()
        }
    }

    fun addItemToWish(id: Int) {
        listItems[id].isInWish = true
        updateItemDB(listItems[id])
        listWish = getItemsWish()
    }

    fun removeItemFromWish(id: Int) {
        listItems[id].isInWish = false
        updateItemDB(listItems[id])
        listWish = getItemsWish()
    }

    fun getItems(): ArrayList<ItemEntity> {
        return listItems
    }

    fun getItemsCart(): ArrayList<ItemEntity> {
        val listCart = ArrayList<ItemEntity>()
        for (item in listItems) {
            if (item.countInCart > 0) {
                listCart.add(item)
            }
        }
        return listCart
    }

    fun getItemsWish(): ArrayList<ItemEntity> {
        val listWish = ArrayList<ItemEntity>()
        for (item in listItems) {
            if (item.isInWish)
                listWish.add(item)
        }
        return listWish
    }

    fun getTotalPriceCart(): Float {
        var total = 0f
        for (item in listItems) {
            total += item.price * item.countInCart
        }
        return total
    }

    fun getItemsOffer(): ArrayList<ItemEntity> {
        val listOffer = ArrayList<ItemEntity>()
        for (item in listItems) {
            if (item.isOffer)
                listOffer.add(item)
        }
        return listOffer
    }

    fun removeAllCartItems() {
        for (item in listItems) {
            if (item.countInCart > 0) {
                item.countInCart = 0
                updateItemDB(item)
            }
        }
    }

    fun showPermissionsDialog(context: Context, supportFragmentManager: FragmentManager) {
        PermissionConsentDialogFragment.open(
            supportFragmentManager,
            { omPermissionAccepted, optPermissionAccepted, nbaPermissionAccepted, tpidPermissionAccepted ->
                val permissions = PermissionsMap()
                permissions.addPermission("CS-OM", omPermissionAccepted)
                permissions.addPermission("CS-OPT", optPermissionAccepted)
                permissions.addPermission("CS-NBA", nbaPermissionAccepted)
                permissions.addPermission("CS-TPID", tpidPermissionAccepted)
                FunnelConnectSDK
                    .updatePermissions(permissions, notificationName, notificationVersion)
                if (nbaPermissionAccepted) {
                    UTIQ.acceptConsent()
                    val isStub = SharedPreferenceUtils.isStubMode(context)
                    UTIQ.startService(isStub)
                } else
                    UTIQ.rejectConsent()
            },
            {
                UTIQ.rejectConsent()
                val permissions = PermissionsMap()
                permissions.addPermission("CS-OM", false)
                permissions.addPermission("CS-OPT", false)
                permissions.addPermission("CS-NBA", false)
                permissions.addPermission("CS-TPID", false)
                FunnelConnectSDK.updatePermissions(
                    permissions,
                    notificationName,
                    notificationVersion
                )
            })
    }

    fun showAbandonedCartDialog(supportFragmentManager: FragmentManager, items: List<ItemEntity>) {
        AbandonedCartDialogFragment.open(
            supportFragmentManager,
            items
        ) { items ->
            for (item in items) {
                addItemToCart(item.itemId)
            }
            navigateAction?.let {
                it.invoke(R.id.navigation_cart)
            }
        }
    }

    fun getBanner(): String {
        var text = ""
        var gson = Gson()
        infoResponse?.let { info ->
            val classOb = InfoResponse::class.java
            classOb?.let { classOnj ->
                var obj: InfoResponse? = gson.fromJson(info, classOnj)
                obj?.let { ob ->
                    ob.attributes?.let { attr ->
                        attr.forEach {
                            text += "&amp;" + it.key + "=" + it.value
                        }
                    }
                }
                Log.d("iran:infoResponse", info)
            }

        }
        if(FunnelConnectSDK.isInitialized()) {
            FunnelConnectSDK.getUserId()?.let { user_id ->
                text += "&amp;rp.user.userId=$user_id"
            }
        }
        text += "&amp;device=android"
        text += "&amp;impression=offer"
        getAbCartId()?.let {
//            Log.d("iraniran:acid", it.toString())
            text += "&amp;ab_cart_id=$it"
        }
//        Log.d("iran:attr", text)
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
                            var src = 'https://ads.celtra.com/fa87bccb/web.js?' + qs + '$text';
                            req.src = src;
                            img.parentNode.insertBefore(req, img.nextSibling);
                        })(this);
                    "/>
                </div>
           </body>
        </html>
        """.trimIndent()
    }

    fun initializeData(db: AppDb, action: ((Int) -> Unit)) {
        navigateAction = action
        this.db = db
        Thread {
//            db.itemDao().removeAllItems()
//            db.acDao().removeAllAc()
            if (db.itemDao().getAllItems().isEmpty()) {
                db.itemDao().saveItems(
                    ItemEntity(
                        0,
                        "Jacob’s Baked Crinklys Cheese",
                        description,
                        60.00f,
                        "crinklys",
                        true
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        1,
                        "Pork Cocktail Sausages, Pack",
                        description,
                        54.00f,
                        "pork",
                        true,
                        false,
                        acId = 123
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        2,
                        "Broccoli and Cauliflower Mix",
                        description,
                        6.00f,
                        "cauliflower"
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        3,
                        "Morrisons Smoked Paprika",
                        description,
                        44.00f,
                        "paprika"
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        4,
                        "Jaffa Tropical Flavour Burst Seedless",
                        description,
                        12.00f,
                        "burst"
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        5,
                        "Pams Chopped Watermelon",
                        description,
                        20.00f,
                        "watermelon"
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        6,
                        "Woolworths Food Flavourburst Seedless Grapes",
                        description,
                        20.00f,
                        "grapes"
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        7,
                        "Ocado Mixed Seedless Grapes",
                        description,
                        50.00f,
                        "mixed"
                    )
                )
                db.itemDao().saveItems(
                    ItemEntity(
                        8,
                        "Whole Foods Market, Organic Coleslaw Mix",
                        description,
                        5.49f,
                        "coleslaw"
                    )
                )
                db.itemDao()
                    .saveItems(ItemEntity(9, "TSARINE Caviar 50g", description, 45.50f, "tsarine"))
                db.itemDao().saveItems(
                    ItemEntity(
                        10,
                        "Knorr Tomato al Gusto All’ Arrabbiata Soße 370g",
                        description,
                        3.99f,
                        "tomato"
                    )
                )
            }
            listItems = db.itemDao().getAllItems() as ArrayList<ItemEntity>
            this.listAc = db.acDao().getAllAcs() as ArrayList<ACEntity>
            listOffers = getItemsOffer()
            if (section == "none")
                action.invoke(R.id.navigation_home)
            section = ""
        }.start()
    }

    fun processCelraAction(
        context: Context,
        data: String,
        supportFragmentManager: FragmentManager
    ) {
        var obj = JSONObject(data)
        obj?.let { ob ->
            ob.getJSONObject("attributes")?.let { attr ->
                if (!attr.isNull("impression")) {
                    attr.getString("impression")?.let { impression ->
                        if (impression == "ShopView") {
                            section = "ShopView"
                            navigateAction?.let {
                                it.invoke(R.id.navigation_shop)
                            }
                        }
                        if (impression == "AbCartView" && listAc.isEmpty()) {
                            Toast.makeText(
                                context,
                                "There isn't abandoned items.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                if (!attr.isNull("item_id")) {
                    attr.getString("item_id")?.let {
                        ItemDescriptionDialogFragment.open(
                            (context as AppCompatActivity).supportFragmentManager,
                            listItems[it.toInt()],
                            {
                                addItemToCart(it.toInt())
                            },
                            {
                                addItemToWish(it.toInt())
                            })
                    }
                }
                if (!attr.isNull("ab_cart_id")) {
                    attr.getString("ab_cart_id")?.let {
                        if (it != "-1") {
                            refreshAcItems(it.toInt()) { list ->
                                showAbandonedCartDialog(supportFragmentManager, list)
                            }
                        }
                    }
                }
                if (!attr.isNull("ident_url")) {
                    attr.getString("ident_url")?.let { url ->
                        if(FunnelConnectSDK.getUserId() != null) {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(browserIntent)
                        }
                        else
                            Toast.makeText(context, "Log in first please.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun addAbandonedCart(items: List<ItemEntity>): Int {
        val acId = getUniqueId()
        db?.let { db ->
            Thread {
                for (item in items) {
                    item.acId = acId
                    db.itemDao().update(item)
                }
                ACEntity(acId).let {
                    db.acDao().saveAC(it)
                    this.listAc.add(it)
                }
            }.start()
        }
        return acId
    }

    fun getUniqueId(): Int {
        return (0..1000).random()
    }

    fun refreshAcItems(acId: Int, action: ((List<ItemEntity>) -> Unit)) {
        db?.let {
            Thread {
                val list = it.itemDao().getAllAcItems(acId)
                action.invoke(list)
            }.start()
        }
    }

    fun handleDeepLink(
        context: Context,
        appLinkData: Uri,
        supportFragmentManager: FragmentManager
    ) {
        val acId = appLinkData.getQueryParameter("ab_cart_id")
        val itemId = appLinkData.getQueryParameter("item_id")
        val impression = appLinkData.getQueryParameter("impression")
        acId?.let {
            refreshAcItems(it.toInt()) { items ->
                showAbandonedCartDialog(supportFragmentManager, items)
            }
            navigateAction?.let { action ->
                action.invoke(R.id.navigation_home)
            }
        }
        itemId?.let {
            ItemDescriptionDialogFragment.open(
                (context as AppCompatActivity).supportFragmentManager,
                listItems[it.toInt()],
                {
                    addItemToCart(it.toInt())
                },
                {
                    addItemToWish(it.toInt())
                })
            navigateAction?.let { action ->
                action.invoke(R.id.navigation_home)
            }
        }
        impression?.let {
            if (it == "ShopView") {
                navigateAction?.let { action ->
                    action.invoke(R.id.navigation_shop)
                }
            }
        }
    }

    fun getAbCartId(): Int? {
        if (this.listAc.isNotEmpty()) {
//            Log.d("iraniranCountAc", this.listAc.size.toString())
            return this.listAc.last().acId
        }
        return null
    }

    fun getUserId(): String? {
        return FunnelConnectSDK.getUserId()
        return ""
    }
}