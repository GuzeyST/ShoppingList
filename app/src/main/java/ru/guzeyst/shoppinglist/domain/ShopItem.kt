package ru.guzeyst.shoppinglist.domain

data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean = true,
    var id: Int = -1
)
