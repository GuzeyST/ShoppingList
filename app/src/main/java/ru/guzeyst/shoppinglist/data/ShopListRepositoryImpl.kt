package ru.guzeyst.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.guzeyst.shoppinglist.UNDEFINED_ID
import ru.guzeyst.shoppinglist.domain.ShopItem
import ru.guzeyst.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoincrement = 0
    private val shopListLD = MutableLiveData<List<ShopItem>>()

    init {
        for(i in 0..10){
            val shopItem = ShopItem("Name $i", i, true)
            addShopItem(shopItem)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == UNDEFINED_ID){
            shopItem.id = autoincrement++
        }
        shopList.add(shopItem)
        updateShopList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateShopList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        deleteShopItem(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateShopList(){
        shopListLD.value = shopList.toList()
    }
}