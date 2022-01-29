package ru.guzeyst.shoppinglist.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.guzeyst.shoppinglist.R
import ru.guzeyst.shoppinglist.databinding.ActivityMainBinding
import ru.guzeyst.shoppinglist.presentation.shopItem.ShopItemActivity
import ru.guzeyst.shoppinglist.presentation.shopItem.ShopItemFragment

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditFinishListener {

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
            if (isOnePaneMode()) {
                startActivity(ShopItemActivity.newIntentAddItem(this))
            }else{
                startFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun startFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isOnePaneMode(): Boolean{
        return binding.fragmentContainer == null
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
            val id = it.id
            if( isOnePaneMode()) {
                startActivity(ShopItemActivity.newIntentEditItem(this, id))
            }else{
                startFragment(ShopItemFragment.newInstanceEditItem(id))
            }
        }
    }

    private fun setupLongClickListener() {
        adapterForRv.shopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    override fun onFinish() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}