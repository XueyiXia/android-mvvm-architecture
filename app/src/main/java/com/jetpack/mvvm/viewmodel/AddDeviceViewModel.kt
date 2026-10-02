package com.jetpack.mvvm.viewmodel

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.mvvm.WifiScanner
import com.jetpack.mvvm.bean.WifiInfo
import com.jetpack.mvvm.ble.BleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddDeviceViewModel(
    private val scanner: WifiScanner,
    private val bleManager: BleManager,
):ViewModel(){


    private val _wifiList= MutableStateFlow<List<WifiInfo>>(emptyList())
    val wifiList= _wifiList.asStateFlow()

    fun scanWifi(){
        viewModelScope.launch{
            val list= scanner.scan()
            _wifiList.value= list

        }
    }




    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
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



}