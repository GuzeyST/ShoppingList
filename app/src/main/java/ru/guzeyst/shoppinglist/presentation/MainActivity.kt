package ru.guzeyst.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import ru.guzeyst.shoppinglist.R
import ru.guzeyst.shoppinglist.databinding.ActivityMainBinding
import ru.guzeyst.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this, {
            Log.d("MyTag", it.toString())
            if(count == 0) {
                count++
                val shopItem = it[0]
                viewModel.changeEnableState(shopItem)
            }
        })
    }
}