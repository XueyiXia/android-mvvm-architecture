package com.jetpack.mvvm.viewmodel

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.framework.mvvm.livedata.IntLiveData
import com.jetpack.mvvm.ble.model.DeviceUiState
import com.jetpack.mvvm.ble.repository.BleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val repository: BleRepository
) : ViewModel() {

    // =====================================================
    // UI State
    // =====================================================

    private val _uiState = MutableStateFlow(DeviceUiState())
    val uiState: StateFlow<DeviceUiState> = _uiState.asStateFlow()

    val onScanClickListener = IntLiveData()
    val onMeasureClickListener = MutableLiveData<View>()
    val onConnectionClickListener = MutableLiveData<View>()

    // =====================================================
    // 初始化
    // =====================================================

    init {

        observeDevices()

        observeConnection()

        observeHealthData()

        observeError()
    }


    // =====================================================
    // 监听扫描设备
    // =====================================================

    private fun observeDevices() {

        viewModelScope.launch {
            repository.devices.collect { devices ->
                if (devices.isNotEmpty()){
                    val deviceName = devices[0].name
                    _uiState.value = _uiState.value.copy(
                        devices = devices,
                        deviceName=deviceName,
                        device= devices[0]


                    )
                }

            }
        }
    }


    // =====================================================
    // 监听 BLE 连接状态
    // =====================================================

    private fun observeConnection() {
        viewModelScope.launch {
            repository.connected.collect { connected ->
                Log.d("observeConnection", " connected = $connected")
                _uiState.value = _uiState.value.copy(connected = connected,connectStatus =if(connected){"已经连接蓝牙"}else{"未连接蓝牙"})
            }
        }
    }


    // =====================================================
    // 监听健康数据
    // =====================================================

    private fun observeHealthData() {

        viewModelScope.launch {

            repository.data.collect { data ->

                _uiState.value =
                    _uiState.value.copy(
                        weight =
                            String.format(
                                "%.1f kg",
                                data.weight
                            ),

                        fat =
                            String.format(
                                "%.1f %%",
                                data.fat
                            ),

                        muscle =
                            String.format(
                                "%.1f kg",
                                data.muscle
                            ),

                        water =
                            String.format(
                                "%.1f %%",
                                data.water
                            ),

                        bmi =
                            String.format(
                                "%.1f",
                                data.bmi
                            ),

                        systolic =
                            data.systolic,

                        diastolic =
                            data.diastolic,

                        heartRate =
                            data.heartRate,

                        oxygen =
                            String.format(
                                "%d %%",
                                data.oxygen
                            ),

                        measuring = false,

                        error = null
                    )
            }
        }
    }


    // =====================================================
    // 监听错误
    // =====================================================

    private fun observeError() {

        viewModelScope.launch {

            repository.error.collect { message ->

                _uiState.value =
                    _uiState.value.copy(
                        measuring = false,
                        error = message
                    )
            }
        }
    }


    // =====================================================
    // 开始扫描
    // =====================================================

    fun startScan() {

        repository.scan()
    }


    // =====================================================
    // 停止扫描
    // =====================================================

    fun stopScan() {

        repository.stopScan()
    }


    // =====================================================
    // 连接设备
    // =====================================================
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(device: BluetoothDevice) {
        repository.connect(device)
    }


    // =====================================================
    // 开始测量
    // =====================================================

    fun startMeasure() {
        if (!_uiState.value.connected) {
            _uiState.value = _uiState.value.copy(error = "请先连接设备")
            return
        }

        _uiState.value =
            _uiState.value.copy(
                measuring = true,
                error = null
            )

        repository.startMeasure()
    }


    // =====================================================
    // 断开连接
    // =====================================================

    fun disconnect() {

        repository.disconnect()
    }


    // =====================================================
    // 清除错误
    // =====================================================

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


    // =====================================================
    // ViewModel 销毁
    // =====================================================

    override fun onCleared() {
        repository.release()
        super.onCleared()
    }
}