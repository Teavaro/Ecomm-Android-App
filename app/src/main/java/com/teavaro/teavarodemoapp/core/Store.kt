package com.teavaro.teavarodemoapp.core

object Store {

    private var listItems: ArrayList<Item> = ArrayList()
    var isLogin = false

    init {
        listItems.add(Item(0, "Jacob’s Baked Crinklys Cheese", 60.00f, "crinklys", true))
        listItems.add(Item(1, "Pork Cocktail Sausages, Pack", 54.00f, "pork", true, false))
        listItems.add(Item(2, "Broccoli and Cauliflower Mix", 6.00f, "cauliflower"))
        listItems.add(Item(3, "Morrisons Creamed Rice Pudding", 44.00f, "paprika"))
        listItems.add(Item(4, "Fresh For The Bold Ground Amazon", 12.00f, "burst"))
        listItems.add(Item(5, "Frito-Lay Doritos & Cheetos Mix", 20.00f, "watermelon"))
        listItems.add(Item(6, "Green Mountain Coffee Roast", 20.00f, "grapes"))
        listItems.add(Item(7, "Nature’s Bakery Whole Wheat Bars", 50.00f, "mixed"))
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
}