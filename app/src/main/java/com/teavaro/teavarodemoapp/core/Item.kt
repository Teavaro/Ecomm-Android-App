package com.teavaro.teavarodemoapp.core

data class Item (
    var id: Int,
    var title: String,
    var price: Float,
    var picture: Int,
    var isOffer: Boolean = false,
    var isInStock: Boolean = true,
    var isWish: Boolean = false,
    var countOnCart: Int = 0
)