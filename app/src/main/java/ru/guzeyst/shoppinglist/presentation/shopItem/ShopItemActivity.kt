package ru.guzeyst.shoppinglist.presentation.shopItem

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.text.set
import androidx.lifecycle.ViewModelProvider
import ru.guzeyst.shoppinglist.*
import ru.guzeyst.shoppinglist.databinding.ActivityShopItemBinding
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel
    private var screenMode = DEFAULT_MODE
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }

        getObservOnError()
        setObservInputText()
        setObservReadyCloseScreen()
    }

    private fun launchAddMode() = with(binding){
        btSave.setOnClickListener{
            val name = tvName.text.toString()
            val count = tvCount.text.toString()
            viewModel.addItem(name, count)
        }
    }

    private fun launchEditMode() = with(binding){
        viewModel.getItem(shopItemId)
        viewModel.shopItem.observe(this@ShopItemActivity, {
            tvName.setText(it.name)
            tvCount.setText(it.count.toString())
        })

        btSave.setOnClickListener{
            viewModel.editItem(tvName.text.toString(), tvCount.text.toString())
        }
    }

    private fun getObservOnError() {
        viewModel.errorInputName.observe(this@ShopItemActivity, {
            binding.tilTitleName.error= if(it) ERROR_INPUT_NAME
            else null
        })
        viewModel.errorInputCount.observe(this@ShopItemActivity, {
            binding.tilTitleCount.error= if(it) ERROR_INPUT_COUNT
            else null
        })
    }

    fun parseIntent(){
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

    private fun setObservInputText() {
        binding.tvName.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
        })

        binding.tvCount.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }
        })
    }

    private fun setObservReadyCloseScreen(){
        viewModel.readyToCloseScreen.observe(this@ShopItemActivity, {
            finish()
        })
    }

    companion object{
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }
}