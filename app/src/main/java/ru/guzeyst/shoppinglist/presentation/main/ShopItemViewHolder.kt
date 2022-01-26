package ru.guzeyst.shoppinglist.presentation.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.guzeyst.shoppinglist.R

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
    val tv_count = itemView.findViewById<TextView>(R.id.tv_count)
}