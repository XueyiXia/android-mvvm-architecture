package com.framework.http.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder


/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明: 请求工具类
 */
object RequestUtils {

    private var instance: RequestUtils? = null

    fun get(): RequestUtils? {
        if (instance == null) {
            synchronized(RequestUtils::class.java) {
                if (instance == null) {
                    instance = RequestUtils
                }
            }
        }
        return instance
    }

    /**
     * 获取BaseUrl
     * 备注:根据完整URL获取BasUrl
     *
     * @param url
     * @return
     */
    fun getBasUrl(baseUrl: String): String {
        var url = baseUrl
        var head = ""
        var index = url.indexOf("://")
        if (index != -1) {
            head = url.substring(0, index + 3)
            url = url.substring(index + 3)
        }
        index = url.indexOf("/")
        if (index != -1) {
            url = url.substring(0, index + 1)
        }
        return head + url
    }

    /**
     * 获取 encode 后 Header 值
     * 备注: OkHttp Header 中的 value 不支持 null, \n 和 中文 等特殊字符
     * 后台解析中文 Header 值需要decode（这个后台处理，前端不用理会）
     *
     * @param value
     * @return
     */
    fun getHeaderValueEncoded(value: Any?): Any {
        if (value == null){
            return "null"
        }
        return if (value is String) {
            val strValue = value.replace("\n", "") //换行符
            var i = 0
            val length = strValue.length
            while (i < length) {
                val c = strValue[i]
                if (c <= '\u001f' || c >= '\u007f') {
                    return try {
                        URLEncoder.encode(strValue, "UTF-8") //中文处理
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                        ""
                    }
                }
                i++
            }
            strValue
        } else {
            value
        }
    }
}