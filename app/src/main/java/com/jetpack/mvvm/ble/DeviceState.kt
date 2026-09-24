package com.jetpack.mvvm.ble




/**
 * 蓝牙设备测量状态
 *
 * 状态流程:
 * DISCONNECTED
 *      ↓
 * SCANNING
 *      ↓
 * CONNECTING
 *      ↓
 * READY
 *      ↓
 * MEASURING
 *      ↓
 * COMPLETE
 *
 * ERROR 可由任意状态进入
 */
enum class DeviceState {

    /**
     * 未连接设备
     *
     * 场景:
     * 1. App首次打开
     * 2. 蓝牙断开
     * 3. 用户主动断开设备
     *
     * UI:
     * 显示设备连接页面
     */
    DISCONNECTED,

    /**
     * 正在扫描蓝牙设备
     *
     * 场景:
     * 用户点击"扫描设备"
     *
     * BLE:
     * BluetoothLeScanner.startScan()
     *
     * UI:
     * 显示搜索动画
     * 显示发现设备信息
     */
    SCANNING,

    /**
     * 正在连接蓝牙设备
     *
     * 场景:
     * 用户点击连接设备
     *
     * BLE:
     * connectGatt()
     * discoverServices()
     *
     * UI:
     * 显示连接中
     */
    CONNECTING,

    /**
     * 蓝牙连接完成，等待测量
     *
     * 场景:
     * 1. GATT连接成功
     * 2. Service发现完成
     * 3. Notify监听开启
     *
     * 此时:
     * 设备可以正常通信
     * 但还没有测量数据
     *
     * UI:
     * 显示:
     * "已连接，请上秤"
     */
    READY,

    /**
     * 正在测量
     *
     * 场景:
     * 1. 用户站上体脂秤
     * 或
     * 2. App发送开始测量指令
     *
     * BLE:
     * 等待设备返回实时数据
     *
     * UI:
     * 显示:
     * 动态圆环
     * 测量动画
     * 请站稳
     */
    MEASURING,

    /**
     * 测量完成
     *
     * 场景:
     * BLE收到完整健康数据
     *
     * 数据:
     * weight
     * fat
     * muscle
     * water
     * bmi
     * heartRate
     * oxygen
     *
     * 后续:
     * 1. 保存Room历史记录
     * 2. 展示健康报告
     */
    COMPLETE,

    /**
     * 异常状态
     *
     * 场景:
     * 1. 蓝牙连接失败
     * 2. Notify开启失败
     * 3. 测量超时
     * 4. 数据解析失败
     *
     * UI:
     * 显示错误提示
     * 提供重新连接操作
     */
    ERROR
}