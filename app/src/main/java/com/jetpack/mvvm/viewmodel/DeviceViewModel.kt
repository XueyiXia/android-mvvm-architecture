package com.jetpack.mvvm.viewmodel

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.framework.mvvm.livedata.IntLiveData
import com.jetpack.mvvm.ble.DeviceState
import com.jetpack.mvvm.ble.model.DeviceUiState
import com.jetpack.mvvm.ble.repository.BleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val repository: BleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeviceUiState())
    val uiState: StateFlow<DeviceUiState> = _uiState.asStateFlow()

    val onScanClickListener = IntLiveData()
    val onMeasureClickListener = MutableLiveData<View>()
    val onConnectionClickListener = MutableLiveData<View>()

    init {
        observeDevices()
        observeConnection()
        observeHealthData()
        observeRssiData()
        observeError()
    }

    private fun observeDevices() {
        viewModelScope.launch {
            repository.devices.collect { devices ->
                if (devices.isNotEmpty()) {
                    val device = devices[0]
                    Log.d("DeviceViewModel", "device= :${device}")
                    _uiState.value = _uiState.value.copy(
                        deviceName = device.name ?: "",
                        deviceState = DeviceState.CONNECTING,
                        device = device

                    )
                }
            }
        }
    }

    private fun observeConnection(){
        viewModelScope.launch {
            repository.connected.collect { connected ->
                if(connected){
                    _uiState.value = _uiState.value.copy(
                        deviceState = DeviceState.READY,
                        statusText = "请站上秤",
                        progress = 0
                    )

                }

            }
        }
    }

    private fun observeHealthData() {
        viewModelScope.launch {
            repository.data.collect { data ->
                Log.d("DeviceViewModel", "data=$data")
                updateHealthData(data)
            }
        }
    }


    private fun observeRssiData() {
        viewModelScope.launch {
            repository.rssi.collect { rssi ->
                Log.d("DeviceViewModel", "rssi=${rssi}")
                _uiState.value = _uiState.value.copy(rssi = rssi)
            }
        }
    }

    private fun observeError() {
        viewModelScope.launch {
            repository.error.collect { message ->
                _uiState.value = _uiState.value.copy(
                    deviceState = DeviceState.ERROR,
                )
            }
        }
    }

    fun startScan() {
        _uiState.value = _uiState.value.copy(
            deviceState = DeviceState.SCANNING
        )
        repository.scan()
    }

    fun stopScan() {
        repository.stopScan()
    }

//    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
//    fun connect(device: BluetoothDevice) {
//        _uiState.value = _uiState.value.copy(
//            deviceState = DeviceState.CONNECTING
//        )
//        repository.connect(device)
//    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connectDevice() {
        _uiState.value.device?.let {
            repository.connect(it)

        } ?: run {
            _uiState.value =
                _uiState.value.copy(
                    statusText = "未发现设备"
                )
        }
    }

    fun startMeasure() {
        _uiState.value = _uiState.value.copy(
            deviceState = DeviceState.MEASURING,
            progress = 10,
            statusText = "正在测量"
        )
        repository.startMeasure()
    }

    private fun updateHealthData(data: DeviceUiState) {
        _uiState.value = _uiState.value.copy(
            deviceState = DeviceState.COMPLETE,
            progress = calculateProgress(data),
            weight = data.weight,
            fat = data.fat,
            muscle = data.muscle,
            water = data.water,
            bmi = data.bmi,
            systolic = data.systolic,
            diastolic = data.diastolic,
            heartRate = data.heartRate,
            oxygen = data.oxygen,
            score = calculateScore(data)
        )
    }

    private fun calculateScore(data: DeviceUiState): Int {
        return 88
    }

    fun disconnect() {
        repository.disconnect()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(

        )
    }


    private fun calculateProgress(
        data: DeviceUiState
    ):Int{


        var progress=0


        if((data.weight.toFloatOrNull() ?: 0f) > 0){
            progress +=20
        }


        if((data.fat.toFloatOrNull() ?: 0f) > 0){
            progress +=20
        }


        if((data.muscle.toFloatOrNull() ?: 0f) > 0){
            progress +=20
        }


        if((data.water.toFloatOrNull() ?: 0f) > 0){
            progress +=20
        }


        if((data.bmi.toFloatOrNull() ?: 0f) > 0){
            progress +=20
        }


        return progress
    }

    override fun onCleared() {
        repository.release()
        super.onCleared()
    }
}