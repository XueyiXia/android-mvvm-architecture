package com.framework.http.interfac

import com.framework.http.utils.TypeUtils
import java.lang.reflect.Type

abstract class HttpResponseListener<T> {

   var type: Type

    constructor() {
        type = TypeUtils.getType(javaClass)
    }

    constructor(type: Type) {
        this.type = type
    }

    open fun onSucceed(data: T, method: String){} //成功的时候回调

    /**
     * http请求开始时回调
     */
    open fun onStart(){}

    /**
     * http请求成功时回调
     */
    abstract fun onNext(response: T?)

    /**
     * http请求失败时回调
     */
    abstract fun onError(e: Throwable?)

    /**
     * http请求完成时回调
     */
    open fun onCompleted(){}

    /**
     * 上传下载进度回调
     * @param readBytes
     * @param totalBytes
     */
    open fun onProgress(readBytes: Long, totalBytes: Long){}
}