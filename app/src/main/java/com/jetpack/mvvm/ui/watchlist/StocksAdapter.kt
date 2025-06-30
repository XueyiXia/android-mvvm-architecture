package com.jetpack.mvvm.ui.watchlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jetpack.mvvm.databinding.ItemPositionBinding
import com.jetpack.mvvm.databinding.ItemStockBinding
import com.jetpack.mvvm.network.data.Quote
import com.jetpack.mvvm.ui.watchlist.PortfolioVH.PositionVH
import com.jetpack.mvvm.ui.watchlist.PortfolioVH.StockVH

import kotlin.collections.mapTo


class StocksAdapter constructor(
  private val listener: QuoteClickListener?=null,
) : ListAdapter<Quote, PortfolioVH>(DiffCallback()) {

  interface QuoteClickListener {
    fun onClickQuoteOptions(
      view: View,
      quote: Quote,
      position: Int
    ) {}

    fun onOpenQuote(
      view: View,
      quote: Quote,
      position: Int
    )
  }

  companion object {
    const val TYPE_STOCK = 1
    const val TYPE_POSITION = 2
  }

  @SuppressLint("LogNotTimber")
  fun refresh() {
//    submitList(xx.getStocks())
  }

  override fun getItemViewType(position: Int): Int {
    val stock = currentList[position]
    return when {
      stock.hasPositions() -> TYPE_POSITION
      else -> TYPE_STOCK
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PortfolioVH {
    val context = parent.context
    val portfolioVH: PortfolioVH = if (viewType == TYPE_POSITION) {
      val binding = ItemPositionBinding.inflate(LayoutInflater.from(context), parent, false)
      PositionVH(binding)
    } else {
      val binding = ItemStockBinding.inflate(LayoutInflater.from(context), parent, false)
      StockVH(binding)
    }
    return portfolioVH
  }

  override fun onBindViewHolder(
    holder: PortfolioVH,
    position: Int
  ) {
    holder.update(currentList[position], listener)
  }

  override fun getItemId(position: Int): Long = position.toLong()


  class DiffCallback : DiffUtil.ItemCallback<Quote>() {
    override fun areItemsTheSame(
      oldItem: Quote,
      newItem: Quote
    ): Boolean {
      return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(
      oldItem: Quote,
      newItem: Quote
    ): Boolean {
      return oldItem == newItem
    }
  }
}
