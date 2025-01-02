package com.jetpack.mvvm.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.R


class SimpleRecyclerAdapter(private val context: Context, var data: List<String>) :
    RecyclerView.Adapter<SimpleHolder?>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val view: View = inflater.inflate(R.layout.item_view, parent, false)
        return SimpleHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        val color = data[position]
        holder.textView.setBackgroundColor(Color.parseColor(color))
        holder.textView.text = "item $position"
        holder.textView.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                context,
                "item clicked: $color",
                Toast.LENGTH_SHORT
            ).show()
        })
    }


}

class SimpleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById<TextView>(R.id.textview)

    init {
        textView.setGravity(Gravity.CENTER)
    }
}
