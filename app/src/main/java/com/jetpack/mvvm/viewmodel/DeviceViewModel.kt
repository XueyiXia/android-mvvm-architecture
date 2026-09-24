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
                        deviceState = DeviceState.CONNECTING

                    )
                }
            }
        }
    }

    private fun observeConnection() {
        viewModelScope.launch {
            repository.connected.collect { connected ->
                Log.d("DeviceViewModel", "connected=$connected")
                _uiState.value = _uiState.value.copy(
                    deviceState = if (connected) {
                        DeviceState.READY
                    } else {
                        DeviceState.DISCONNECTED
                    }
                )
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
//        _uiState.value.connectStatus?.let {
//            repository.connect(it)
//
//        } ?: run {
//            _uiState.value =
//                _uiState.value.copy(
//                    statusText = "未发现设备"
//                )
//        }
    }

    fun startMeasure() {
//        if (!_uiState.value.) {
//            _uiState.value = _uiState.value.copy(
//                deviceState = DeviceState.ERROR,
//            )
//            return
//        }

        _uiState.value = _uiState.value.copy(
            deviceState = DeviceState.MEASURING
        )

        repository.startMeasure()
    }

    private fun updateHealthData(data: DeviceUiState) {
        _uiState.value = _uiState.value.copy(
            deviceState = DeviceState.COMPLETE,
            weight = data.weight.toString(),
            fat = data.fat.toString(),
            muscle = data.muscle.toString(),
            water = data.water.toString(),
            bmi = data.bmi.toString(),
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

    override fun onCleared() {
        repository.release()
        super.onCleared()
    }
}