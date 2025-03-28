package com.framework.http.api

import java.io.Serializable

class BaseResponse<T> : Serializable {
    private var isSuccess = false
    private var code :Any = 0
    private var message: String? = null
    private var data: T? = null


    fun getMessage(): String? {
        return if (message == null) "" else message
    }

    fun setMessage(msg: String) {
        message = msg
    }

    fun getCode(): Any {
        return code
    }

    fun setCode(code: Any) {
        this.code = code
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T?) {
        this.data = data
    }

    fun isSuccess(): Boolean {
        return isSuccess
    }

    fun setSuccess(success: Boolean) {
        isSuccess = success
    }
}