package com.framework.http.converter

import okhttp3.ResponseBody
import java.lang.reflect.Type

interface IConverter<T> {
    @Throws(Exception::class)
    fun convert(body: ResponseBody, type: Type): T
}