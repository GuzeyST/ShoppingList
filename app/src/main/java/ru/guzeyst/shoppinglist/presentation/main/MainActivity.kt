package ru.guzeyst.shoppinglist.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.guzeyst.shoppinglist.databinding.ActivityMainBinding
import ru.guzeyst.shoppinglist.presentation.shopItem.ShopItemActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapterForRv: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this, {
            adapterForRv.submitList(it)
        })

        binding.fbAddNewItem.setOnClickListener{
            startActivity(ShopItemActivity.newIntentAddItem(this))
        }
    }

    fun startAddActivity(): (Unit) -> Unit{
         val funStartActivity: (Unit) -> Unit = {

        }
        return funStartActivity
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        adapterForRv = ShopListAdapter()

        with(recyclerView) {
            adapter = adapterForRv
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.IS_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.IS_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(recyclerView)
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapterForRv.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupClickListener() {
        adapterForRv.shopItemClickListener = {
            startActivity(ShopItemActivity.newIntentEditItem(this, it.id))
        }
    }

    private fun setupLongClickListener() {
        adapterForRv.shopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }
}