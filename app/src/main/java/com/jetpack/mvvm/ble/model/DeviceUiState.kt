package com.jetpack.mvvm.ble.model

import android.bluetooth.BluetoothDevice
import com.jetpack.mvvm.ble.DeviceState


data class DeviceUiState(

    val connectStatus:String = "未连接",
    val deviceName:String = "",
    val deviceState: DeviceState = DeviceState.DISCONNECTED,
    val device: BluetoothDevice ? =null,
    val progress:Int = 0,
    val rssi:Int = 0,
    val statusText:String = "未连接设备",
    val weight:String = "0",
    val fat:String = "0",
    val muscle:String = "0",
    val water:String = "0",
    val bmi:String = "0",
    val systolic:String = "0",
    val diastolic:String = "0",
    val heartRate:String = "0",
    val oxygen:String = "0",
    val score:Int = 0

)
