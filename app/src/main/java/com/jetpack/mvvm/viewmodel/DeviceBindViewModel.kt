package com.jetpack.mvvm.viewmodel


import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.mvvm.bean.BleDevice
import com.jetpack.mvvm.repository.ble.BleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class DeviceBindViewModel(
    private val repository:BleRepository
):ViewModel(){

    val devices=
        repository.devices.map { list->
            list.map {
                Pair(
                    BleDevice(
                        name=it.name,
                        address=it.address
                    ),
                    it
                )
            }
        }

    private val _connectState=MutableStateFlow(DeviceConnectState.IDLE)
    val connectState:StateFlow<DeviceConnectState> = _connectState

    fun startScan(){
        viewModelScope.launch{
            _connectState.value=DeviceConnectState.SCANNING
            repository.scan()
        }
    }

    fun connectDevice(device: BluetoothDevice){
        viewModelScope.launch{
            _connectState.value=DeviceConnectState.CONNECTING
            repository.connect(device)
            repository.connected.collectLatest{
                if(it){
                    _connectState.value=DeviceConnectState.WAIT_WIFI
                    observeWifiStatus()
                }
            }
        }
    }

    private fun observeWifiStatus(){
        viewModelScope.launch{
            repository.wifiStatus.collectLatest{
                when{
                    it.contains("CONNECTED")->{
                        _connectState.value=DeviceConnectState.WIFI_CONNECTED
                    }
                    it.contains("NO_CONFIG")||
                            it.contains("NO_SSID")||
                            it.contains("PASSWORD_ERROR")->{
                        _connectState.value=DeviceConnectState.NEED_WIFI_CONFIG
                    }
                }
            }
        }
    }

    fun sendWifiConfig(ssid:String,password:String){
        repository.sendWifiConfig(ssid,password)
    }

    fun stopScan(){
        repository.stopScan()
    }

    override fun onCleared(){
        repository.release()
        super.onCleared()
    }
}