package com.framework.http.function

import com.framework.http.observer.HttpObserver
import io.reactivex.rxjava3.functions.Action


class OnDisposeAction constructor(private val httpObserver: HttpObserver<Any>?) : Action {
    @Throws(Exception::class)
    override fun run() {
        //Dispose
        httpObserver?.dispose()
    }
}