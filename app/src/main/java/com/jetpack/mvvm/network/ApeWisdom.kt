package com.jetpack.mvvm.network

import com.jetpack.mvvm.network.data.TrendingResult
import retrofit2.http.GET

interface ApeWisdom {

  @GET("filter/stocks")
  suspend fun getTrendingStocks(): TrendingResult
}