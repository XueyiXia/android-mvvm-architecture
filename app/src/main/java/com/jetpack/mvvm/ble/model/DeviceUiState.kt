package com.jetpack.mvvm.ble.model

import android.bluetooth.BluetoothDevice

data class DeviceUiState(

    val devices: List<BluetoothDevice> = emptyList(),
    // 连接状态
    val connected:Boolean = false,

    val connectStatus: String = "未连接",

    val device: BluetoothDevice?=null,

    // 蓝牙设备名称
    val deviceName:String = "--",


    // 体重
    val weight:String = "-- kg",


    // 体脂
    val fat:String = "-- %",


    // 肌肉
    val muscle:String = "-- kg",


    // 电量
    val battery:String = "-- %",


    val water: String = "--",
    val bmi: String = "--",

    val systolic: String = "--",
    val diastolic: String = "--",

    val heartRate: String = "--",
    val oxygen: String = "--",

    val measuring: Boolean = false,

    val error: String? = null

)
