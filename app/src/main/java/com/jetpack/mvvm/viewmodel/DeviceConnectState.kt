package com.jetpack.mvvm.viewmodel


enum class DeviceConnectState {

    IDLE,

    SCANNING,

    CONNECTING,

    BLE_CONNECTED,

    WAIT_WIFI,

    WIFI_CONNECTED,

    NEED_WIFI_CONFIG,

    ERROR

}