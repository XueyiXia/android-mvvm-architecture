package com.framework.http.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:57
 * @说明:
 */
@SuppressLint("StaticFieldLeak")
class RxHttpConfigure {

    private object Config {
        val holder = RxHttpConfigure()
    }

    companion object {
        fun getInstance()=Config.holder
    }

    /**
     * 全局上下文
     */
    private var context: Context? = null

    /**
     * 请求基础路径
     */
    private var baseUrl: String? = null

    /**
     * 超时时长
     */
    private var timeout: Long = 60

    /**
     * 时间单位
     */
    private lateinit var timeUnit: TimeUnit

    /*Handler*/
    /*全局Handler*/
    private var handler: Handler? = null

    /**
     * 请求参数
     */
    private var baseParameter: TreeMap<String, Any>? = null

    /**
     * 请求头
     */
    private var header: MutableMap<String, Any>? = TreeMap()

    /**
     * 是否显示Log
     */
    private var isShowLog: Boolean = false

    private var downloadConfigure:DownloadConfigure?=null

    private lateinit var lifecycleOwner: LifecycleOwner

    fun init(app: Application): RxHttpConfigure {
        context = app
        handler = Handler(Looper.getMainLooper())
        return this
    }


    /**
     *
     * @param baseUrl String?
     * @return RxHttpConfigure
     */
    fun setBaseUrl(baseUrl: String): RxHttpConfigure {
        this.baseUrl = baseUrl
        return this
    }

    fun getBaseUrl(): String ?{
        return baseUrl
    }


    /**
     * 设置基础参数
     * @param parameter TreeMap<String, Any>?
     * @return RxHttpConfigure
     */
    fun setBaseParameter(parameter: TreeMap<String, Any>?): RxHttpConfigure {
        baseParameter = parameter
        return this
    }

    fun getBaseParameter():TreeMap<String, Any>?{
        return baseParameter
    }


    /**
     * 基础Header
     * @param header MutableMap<String, Any>?
     * @return RxHttpConfigure
     */
    fun setBaseHeader(header: MutableMap<String, Any>?): RxHttpConfigure {
        this.header = header
        return this
    }

    val getBaseHeader: MutableMap<String, Any>
        get() = if (header == null) TreeMap() else header!!


    /**
     * 超时时长
     * @param timeout Long
     * @return RxHttpConfigure
     */
    fun setTimeout(timeout: Long): RxHttpConfigure {
        this.timeout = timeout
        return this
    }

    fun getTimeout():Long{
        return this.timeout
    }


    /**
     * 是否显示LOG
     * @param showLog Boolean
     * @return RxHttpConfigure
     */
    fun showLog(showLog: Boolean): RxHttpConfigure {
        isShowLog = showLog
        return this
    }

    /**
     * 时间单位
     * @param timeUnit TimeUnit
     * @return RxHttpConfigure
     */
    fun setTimeUnit(timeUnit: TimeUnit): RxHttpConfigure {
        this.timeUnit = timeUnit
        return this
    }

    fun getTimeUnit():TimeUnit{
        return this.timeUnit
    }


    fun getHandler(): Handler? {
        return handler
    }

    /**
     * 下载配置
     * @param downloadConfigure DownloadConfigure
     * @return RxHttpConfigure
     */
    fun setDownloadConfigure(downloadConfigure: DownloadConfigure): RxHttpConfigure {
        this.downloadConfigure = downloadConfigure
        return this
    }

    fun setLifecycle(lifecycleOwner: LifecycleOwner): RxHttpConfigure {
        this.lifecycleOwner = lifecycleOwner
        return this
    }

    fun getDownloadConfigure()=downloadConfigure
}