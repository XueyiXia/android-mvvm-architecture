package com.jetpack.mvvm.ui.home

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.R
import com.jetpack.mvvm.databinding.ItemMenuBinding
import com.jetpack.mvvm.model.Menu
import com.jetpack.mvvm.utils.PokemonColorUtil

class MenuAdapter(
    private val list: List<Menu>,
    private val context: Context
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mBinding = ItemMenuBinding.bind(itemView)
        fun bindView(item: Menu) {
            mBinding.textViewName.text = itemView.context.getString(item.name)

            val color = PokemonColorUtil(itemView.context).convertColor(item.color)
            mBinding.relativeLayoutBackground.background.colorFilter =
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
