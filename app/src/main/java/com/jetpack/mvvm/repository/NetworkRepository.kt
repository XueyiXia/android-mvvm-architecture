package com.jetpack.mvvm.repository

import android.app.Application
import com.framework.http.config.RxHttpConfigure
import com.framework.http.utils.HttpConstants
import com.rxjava_retrofit.HttpApi
import java.util.concurrent.TimeUnit


/**
 * @author: xueyi.xia
 * @date: 2025-03-28
 * @time: 12:47
 * @说明:
 */
class NetworkRepository(application:Application) {
    val headerMap :MutableMap<String, Any> = mutableMapOf()
    init {
        headerMap["Content-Type"] = "application/x-www-form-urlencoded" //默认的编码方式
        headerMap["Connection"] = "Keep-Alive"
        headerMap["Accept-Language"] = "zh-cn"
        headerMap["Accept"] = "Application/Json"
        //必须初始化
        RxHttpConfigure.getInstance()
            .setBaseUrl(HttpApi.BASE_URL)
            .setBaseHeader(headerMap)
            .setTimeout(HttpConstants.TIME_OUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .showLog(true)
            .init(application)
    }
}