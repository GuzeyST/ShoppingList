package ru.guzeyst.shoppinglist.presentation.shopItem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.guzeyst.shoppinglist.data.ShopListRepositoryImpl
import ru.guzeyst.shoppinglist.domain.*

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopItem = GetShopItemUseCase(repository)
    private val editShopItem = EditShopItemUseCase(repository)
    private val addShopItem = AddShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _readyToClose = MutableLiveData<Unit>()
    val readyToCloseScreen : LiveData<Unit>
    get() = _readyToClose

    fun getItem(itemId: Int) {
        val item = getShopItem.getShopItem(itemId)
        _shopItem.value = item
    }

    fun editItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseInt(inputCount)
        val fieldsValue = validateInput(name, count)
        if (fieldsValue) {
            _shopItem.value?.let {
                val item = it.copy(name, count)
                editShopItem.editShopItem(item)
                finishWork()
            }
        }
    }

    fun addItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseInt(inputCount)
        val fieldsValue = validateInput(name, count)
        if (fieldsValue) {
            addShopItem.addShopItem(ShopItem(name, count))
        }
        finishWork()
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseInt(inputInt: String?): Int {
        return try {
            inputInt?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            Log.d("MyTag", e.message ?: "")
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    fun finishWork(){
        _readyToClose.value = Unit
    }
}