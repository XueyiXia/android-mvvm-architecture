package com.jetpack.mvvm.repository.ble

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.gson.Gson
import com.jetpack.mvvm.MqttManager
import com.jetpack.mvvm.ble.BleManager
import com.jetpack.mvvm.ble.model.DeviceUiState
import kotlinx.coroutines.flow.map




class BleRepository(
    private val bleManager:BleManager,
    private val mqttManager:MqttManager
){

    private val gson=Gson()

    val devices=bleManager.devices

    val connected=bleManager.connected

    val rssi=bleManager.rssi


    val data=
        bleManager.data.map{json->
            Log.d("getJSon","==:${json}")
            gson.fromJson(
                json,
                DeviceUiState::class.java
            )
        }


    val error=bleManager.error



    // ===============================
    // BLE扫描
    // 原逻辑保留
    // ===============================

    fun scan(){
        bleManager.startScan()
    }


    fun stopScan(){
        bleManager.stopScan()
    }


    // ===============================
    // BLE连接
    // 原逻辑保留
    // ===============================

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(device:BluetoothDevice){
        bleManager.connect(device)
    }


        // ===============================
        // WIFI状态
        // 新增
        // ===============================

    val wifiStatus= bleManager.wifiStatus

    // ===============================
    // WIFI配置
    // 新增
    // ===============================

    fun sendWifiConfig(
        ssid:String,
        password:String
    ){

        val json="""
                    {
                    "type":"wifi",
                    "ssid":"$ssid",
                    "password":"$password"
                    }
                    """.trimIndent()


        bleManager.writeCommand(json)

    }



// ===============================
// 测量
// 原逻辑保留
// ===============================

    fun startMeasure(){
        bleManager.startMeasure()
    }


    fun disconnect(){
        bleManager.disconnect()
    }


    fun release(){
        bleManager.release()
    }

}