package com.jetpack.mvvm.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.R


class BannerRecyclerAdapter(private var dataList: MutableList<String>) : RecyclerView.Adapter<BannerRecyclerAdapter.BannerViewHolder?>() {



    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_header_view, parent, false)
        return BannerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }



    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val color = dataList[position]
        holder.textView.setBackgroundColor(Color.parseColor(color))
        holder.textView.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                holder.textView.context,
                "banner clicked: $color",
                Toast.LENGTH_SHORT
            ).show()
        })
    }


}


