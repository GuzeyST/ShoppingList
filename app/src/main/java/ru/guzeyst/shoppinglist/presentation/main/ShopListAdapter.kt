package ru.guzeyst.shoppinglist.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.guzeyst.shoppinglist.R
import ru.guzeyst.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopListDiffCallback()) {

    var shopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var shopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val idLayout = when (viewType) {
            IS_ENABLED -> R.layout.shop_item_enabled
            IS_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown view type $viewType")
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(idLayout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopitem = getItem(position)
        holder.tv_name.text = "${shopitem.name} ${shopitem.enabled}"
        holder.tv_count.text = shopitem.count.toString()
        with(holder.itemView) {
            setOnLongClickListener {
                shopItemLongClickListener?.invoke(shopitem)
                true
            }

            setOnClickListener {
                shopItemClickListener?.invoke(shopitem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            IS_ENABLED
        } else {
            IS_DISABLED
        }
    }

    companion object {
        const val IS_ENABLED = 1
        const val IS_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }
}
