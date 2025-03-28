package com.framework.http.interfac

/**
 * @author: xiaxueyi
 * @date: 2021-10-23
 * @time: 09:15
 * @说明: 服务器数据回调,
 */
open class SimpleResponseListener<T> : HttpResponseListener<T>() {


    override fun onStart() {
        super.onStart()
    }

    override fun onCompleted() {
        super.onCompleted()
    }

    override fun onNext(response: T?) {

    }

    override fun onError(e: Throwable?) {

    }
}