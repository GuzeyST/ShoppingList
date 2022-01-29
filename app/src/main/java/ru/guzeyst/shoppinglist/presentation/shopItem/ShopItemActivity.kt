package ru.guzeyst.shoppinglist.presentation.shopItem

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.guzeyst.shoppinglist.*
import ru.guzeyst.shoppinglist.databinding.ActivityShopItemBinding


class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditFinishListener {
    private lateinit var binding: ActivityShopItemBinding
    private var screenMode = DEFAULT_MODE
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        if(savedInstanceState == null) launchMode()
    }

    private fun launchMode(){
        val fragment = when(screenMode){
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException(ERROR_UNKNOWN_EXTRA_MODE)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException(ERROR_ABSENT_EXTRA_MODE)
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if(mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException(ERROR_UNKNOWN_EXTRA_MODE + "$mode")
        }

        screenMode = mode

       if(screenMode == MODE_EDIT){
           if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
               throw RuntimeException(ERROR_ABSENT_ITEM_ID)
           }
           shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
       }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }

    override fun onFinish() {
        supportFragmentManager.popBackStack()
    }
}