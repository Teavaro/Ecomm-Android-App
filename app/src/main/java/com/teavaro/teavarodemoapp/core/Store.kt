package com.teavaro.teavarodemoapp.core

object Store {

    private lateinit var listItems: ArrayList<Item>
    private lateinit var listCart: ArrayList<Item>
    private lateinit var listWish: ArrayList<Item>
    private var isInitialized = false

    fun initialize(){
        listItems = ArrayList()
        listCart = ArrayList()
        listWish = ArrayList()
        listItems.add(Item(0, "puerco", 23.4f, 0))
        listItems.add(Item(1, "picadillo", 20.1f, 1))
        listItems.add(Item(2, "leche", 15.6f, 2))
        isInitialized = true
    }

    fun addItemToCart(id: Int){
        if(isInitialized)
            listCart.add(listItems[id])
    }

    fun removeItemFromCart(id: Int){
        if(isInitialized)
            listCart.removeAt(id)
    }

    fun addItemToWish(id: Int){
        if(isInitialized) {
            listItems[id].isWish = true
            listWish.add(listItems[id])
        }
    }

    fun removeItemFromWish(id: Int){
        if(isInitialized) {
            listItems[id].isWish = false
            listWish.removeAt(id)
        }
    }

    fun getItems(): ArrayList<Item>{
        return listItems
    }

    fun getItemsCart(): ArrayList<Item>{
        return listCart
    }

    fun getItemsWish(): ArrayList<Item>{
        return listWish
    }
}