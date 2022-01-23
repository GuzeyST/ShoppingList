package ru.guzeyst.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.guzeyst.shoppinglist.R
import ru.guzeyst.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object{
        const val IS_ENABLED = 1
        const val IS_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }

    var count = 0
    var shopList = listOf<ShopItem>()
    set(value) {
       field = value
       notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("MyTag", "Create view holder ${++count}")
        val idLayout = when (viewType){
            IS_ENABLED -> R.layout.shop_item_enabled
            IS_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknow view type $viewType")
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(idLayout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopitem = shopList[position]
        holder.tv_name.text = "${shopitem.name} ${shopitem.enabled}"
        holder.tv_count.text = shopitem.count.toString()
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(shopList[position].enabled){
            IS_ENABLED
        }else{
            IS_DISABLED
        }
    }

    class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_count = itemView.findViewById<TextView>(R.id.tv_count)
    }
}
