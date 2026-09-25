package com.jetpack.mvvm.wifi

import com.jetpack.mvvm.net.RetrofitClient

class WifiRepository {
    suspend fun measure() = RetrofitClient.api.measure()
}