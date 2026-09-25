package com.jetpack.mvvm.net

import com.jetpack.mvvm.ble.model.DeviceUiState

import retrofit2.http.GET

interface ScaleApi {

    /**
     * 对应 ESP32:
     * server.on("/measure",HTTP_GET,handleMeasure)
     *
     * 生成模拟测量数据
     */
    @GET("/measure")
    suspend fun measure(): DeviceUiState


    /**
     * 对应 ESP32:
     * server.on("/data",HTTP_GET,handleData)
     *
     * 获取当前保存的数据
     */
    @GET("/data")
    suspend fun getData():DeviceUiState
}