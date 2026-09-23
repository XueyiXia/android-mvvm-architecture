package com.jetpack.mvvm.ble.repository


import android.Manifest
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.framework.mvvm.utils.PermissionManager
import com.google.gson.Gson
import com.jetpack.mvvm.ble.BleManager
import com.jetpack.mvvm.ble.model.DeviceUiState
import kotlinx.coroutines.flow.map
import kotlin.jvm.java


class BleRepository(
    private val bleManager: BleManager
) {

    private val gson = Gson()
    val devices = bleManager.devices
    val connected = bleManager.connected

    val data =
        bleManager.data
            .map { json ->
                Log.d("getJSon","${json}")
                gson.fromJson(
                    json,
                    DeviceUiState::class.java
                )
            }

    val error = bleManager.error
    fun scan() {
        bleManager.startScan()
    }

    fun stopScan() {
        bleManager.stopScan()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(device: BluetoothDevice) {
        bleManager.connect(device)

    }

    fun startMeasure() {
        bleManager.startMeasure()
    }

    fun disconnect() {
        bleManager.disconnect()
    }

    fun release() {
        bleManager.release()
    }
}