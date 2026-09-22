package com.jetpack.mvvm.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class BleManager(
    private val context: Context
) {

    companion object {

        const val DEVICE_NAME = "ESP32_HEALTH"
        private val SERVICE_UUID =
            UUID.fromString(
                "12345678-1234-5678-1234-56789abcdef0"
            )

        private val DATA_UUID =
            UUID.fromString(
                "12345678-1234-5678-1234-56789abcdef1"
            )

        private val COMMAND_UUID =
            UUID.fromString(
                "12345678-1234-5678-1234-56789abcdef2"
            )

        private val CCCD_UUID =
            UUID.fromString(
                "00002902-0000-1000-8000-00805f9b34fb"
            )
    }


    private val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothGatt: BluetoothGatt? = null
    private var dataCharacteristic: BluetoothGattCharacteristic? = null
    private var commandCharacteristic: BluetoothGattCharacteristic? = null

    // --------------------------------------------------
    // 扫描设备
    // --------------------------------------------------
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices = _devices.asStateFlow()

    // --------------------------------------------------
    // 连接状态
    // --------------------------------------------------
    private val _connected = MutableStateFlow(false)
    val connected = _connected.asStateFlow()

    // --------------------------------------------------
    // BLE 数据
    // --------------------------------------------------
    private val _data = MutableSharedFlow<String>(extraBufferCapacity = 20)
    val data = _data.asSharedFlow()

    // --------------------------------------------------
    // 错误
    // --------------------------------------------------

    private val _error = MutableSharedFlow<String>(extraBufferCapacity = 10)
    val error = _error.asSharedFlow()

    // ==================================================
    // 扫描
    // ==================================================

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (!hasScanPermission()) {
            emitError("缺少蓝牙扫描权限")
            return
        }
        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (scanner == null) {
            emitError("BluetoothLeScanner 不可用")
            return
        }

        _devices.value = emptyList()

        scanner.startScan(
            scanCallback
        )
    }

    // ==================================================
    // 停止扫描
    // ==================================================

    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (!hasScanPermission()) {
            return
        }
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }

    // ==================================================
    // ScanCallback
    // ==================================================

    private val scanCallback = object : ScanCallback() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceName = device.name ?: return
            if (deviceName ==DEVICE_NAME) {
                stopScan()
                val current = _devices.value.toMutableList()
                Log.d("current", deviceName)
                if (current.none { it.address == device.address }) {
                    current.add(device)
                    _devices.value = current
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            emitError("BLE 扫描失败：$errorCode")
        }
    }

    // ==================================================
    // 连接
    // ==================================================

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {
        if (!hasConnectPermission()) {
            emitError("缺少蓝牙连接权限")
            return
        }

        stopScan()
        bluetoothGatt?.close()
        bluetoothGatt = null
        _connected.value = false
        bluetoothGatt = device.connectGatt(
            context,
            false,
            gattCallback,
            BluetoothDevice.TRANSPORT_LE
        )
    }

    // ==================================================
    // GATT Callback
    // ==================================================

    private val gattCallback = object : BluetoothGattCallback() {

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                _connected.value = true
                val success = gatt.requestMtu(247)
                Log.d("requestMtu", "requestMtu result = $success")
                if (hasConnectPermission()) {
                    gatt.discoverServices()
                }

            } else {
                _connected.value = false
                emitError("BLE 连接断开，status=$status")
                closeGatt()
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                emitError("Service 发现失败：$status")
                return
            }

            val service = gatt.getService(SERVICE_UUID)
            if (service == null) {
                emitError("找不到 Health Service")
                return
            }

            dataCharacteristic = service.getCharacteristic(DATA_UUID)
            commandCharacteristic = service.getCharacteristic(COMMAND_UUID)
            if (dataCharacteristic == null) {
                emitError("找不到 Data Characteristic")
                return
            }

            if (commandCharacteristic == null) {
                emitError("找不到 Command Characteristic")
                return
            }
            enableNotification(gatt)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            if (descriptor.uuid == CCCD_UUID) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    println("BLE Notify ENABLED")
                } else {
                    emitError("Notify 开启失败：$status")
                }
            }
        }

        @Deprecated("Deprecated in API 33")
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            if (characteristic.uuid == DATA_UUID) {
                val value = characteristic.value?.toString(Charsets.UTF_8) ?: return
                println("BLE DATA: $value")
                _data.tryEmit(value)
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            if (characteristic.uuid == DATA_UUID) {
                val data = value.toString(Charsets.UTF_8)
                println("BLE DATA: $data")
                _data.tryEmit(data)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (characteristic.uuid == COMMAND_UUID) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    println("COMMAND WRITE SUCCESS")
                } else {
                    emitError("Command 写入失败：$status")
                }
            }
        }
    }

    // ==================================================
    // 开启 Notify
    // ==================================================

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun enableNotification(
        gatt: BluetoothGatt
    ) {

        val characteristic =
            dataCharacteristic ?: return


        // 开启本地通知
        gatt.setCharacteristicNotification(
            characteristic,
            true
        )


        val descriptor =
            characteristic.getDescriptor(
                CCCD_UUID
            )

        if (descriptor == null) {
            Log.e(
                "BLE",
                "CCCD不存在"
            )

            return
        }


        val value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            // Android 13+
            val result =
                gatt.writeDescriptor(
                    descriptor,
                    value
                )


            Log.d(
                "BLE",
                "Android13 writeDescriptor result=$result"
            )


        } else {

            // Android 12及以下
            descriptor.value = value
            val result = gatt.writeDescriptor(descriptor)
            Log.d(
                "BLE",
                "旧版本 writeDescriptor result=$result"
            )
        }
    }

    // ==================================================
    // 开始测量
    // ==================================================

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun startMeasure() {

        val gatt =
            bluetoothGatt ?: return


        val characteristic =
            commandCharacteristic
                ?: run {

                    Log.e(
                        "BLE",
                        "Command Characteristic 不存在"
                    )

                    return
                }


        val value =
            "START_MEASURE"
                .toByteArray(
                    Charsets.UTF_8
                )


        if (Build.VERSION.SDK_INT>= 34) {


            // Android 13+
            val result =
                gatt.writeCharacteristic(
                    characteristic,
                    value,
                    BluetoothGattCharacteristic
                        .WRITE_TYPE_DEFAULT
                )


            Log.d(
                "BLE",
                "API33 write result=$result"
            )


        } else {


            // Android 12及以下
            characteristic.value = value


            characteristic.writeType =
                BluetoothGattCharacteristic
                    .WRITE_TYPE_DEFAULT


            val result =
                gatt.writeCharacteristic(
                    characteristic
                )


            Log.d(
                "BLE",
                "legacy write result=$result"
            )
        }
    }

    // ==================================================
    // 断开
    // ==================================================

    @SuppressLint("MissingPermission")
    fun disconnect() {
        bluetoothGatt?.disconnect()
    }

    // ==================================================
    // 释放
    // ==================================================

    fun release() {
        stopScan()
        closeGatt()
    }

    @SuppressLint("MissingPermission")
    private fun closeGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
        dataCharacteristic = null
        commandCharacteristic = null
        _connected.value = false
    }

    // ==================================================
    // 权限
    // ==================================================

    private fun hasScanPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun hasConnectPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED

        } else {
            true
        }
    }

    private fun emitError(message: String) {
        println("BLE ERROR: $message")
        _error.tryEmit(message)
    }
}