package com.teavaro.teavarodemoapp.core

object Store {

    private var listItems: ArrayList<Item> = ArrayList()
    private var isInitialized = false

    init {
        listItems.add(Item(0, "puerco", 23.4f, 0, true))
        listItems.add(Item(1, "picadillo", 20.1f, 1, true, false))
        listItems.add(Item(2, "leche", 15.6f, 2))
        listItems.add(Item(3, "cereal", 11.2f, 3))
        listItems.add(Item(4, "pollo", 4.3f, 4))
        isInitialized = true
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
}