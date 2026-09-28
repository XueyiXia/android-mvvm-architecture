package com.jetpack.mvvm.repository.wifi

import com.jetpack.mvvm.net.RetrofitClient

class WifiRepository {
    suspend fun measure() = RetrofitClient.api.measure()
}