package com.jetpack.mvvm.network.data

import com.jetpack.mvvm.AppPreferences

class PriceFormat(
  val currencyCode: String,
  val symbol: String,
  val prefix: Boolean = true
) {
  fun format(price: Float): String {
    val priceString = AppPreferences.SELECTED_DECIMAL_FORMAT.format(price)
    return if (prefix) {
      "$symbol$priceString"
    } else {
      "$priceString$symbol"
    }
  }
}