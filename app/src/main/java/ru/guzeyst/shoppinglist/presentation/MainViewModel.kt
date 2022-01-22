package ru.guzeyst.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.guzeyst.shoppinglist.data.ShopListRepositoryImpl
import ru.guzeyst.shoppinglist.domain.DeleteShopItemUseCase
import ru.guzeyst.shoppinglist.domain.EditShopItemUseCase
import ru.guzeyst.shoppinglist.domain.GetShopListUseCase
import ru.guzeyst.shoppinglist.domain.ShopItem

class MainViewModel: ViewModel() {

    val shopList = MutableLiveData<List<ShopItem>>()
    private val repository = ShopListRepositoryImpl
    private val getShopList = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    fun getShopList(){
        shopList.value = getShopList.getShopList()
    }

    fun changeEnableState(shopItem: ShopItem){
        val newShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newShopItem)
        getShopList()
    }

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }
}