package com.teavaro.teavarodemoapp.core

object Store {

    private var listItems: ArrayList<Item> = ArrayList()
    private var isInitialized = false

    init {
        listItems.add(Item(0, "puerco", 23.4f, 0))
        listItems.add(Item(1, "picadillo", 20.1f, 1))
        listItems.add(Item(2, "leche", 15.6f, 2))
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
}