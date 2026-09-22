package com.jetpack.mvvm.ble.interfac

import com.jetpack.mvvm.ble.model.DeviceUiState

interface BleCallback {

    fun onDeviceFound(device: DeviceUiState)

    fun onConnected()

    fun onDisconnected()

    fun onServicesReady()

    fun onDataReceived(data: ByteArray)

    fun onError(message: String)
}