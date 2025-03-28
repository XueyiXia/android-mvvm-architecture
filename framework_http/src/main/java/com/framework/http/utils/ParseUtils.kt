package com.framework.http.utils

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * @author: xiaxueyi
 * @date: 2023-03-15
 * @time: 17:40
 * @说明: 解析数据工具类
 */
object ParseUtils {


    @Throws(Exception::class)
    fun <T> parseResponse(data: String?, rawType: Type): T? {
        return if (rawType === String::class.java) {
            data as T
        } else if (rawType === JSONObject::class.java) {
            data?.let { JSONObject(it) } as T
        } else if (rawType === JSONArray::class.java) {
            JSONArray(data) as T
        } else {
            return Gson().fromJson(data, rawType)
        }
    }
}