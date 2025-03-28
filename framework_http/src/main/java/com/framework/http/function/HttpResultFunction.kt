package com.framework.http.function

import com.framework.http.exception.EventException
import com.framework.http.exception.ExceptionEngine
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function


/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明: http结果处理函数，进行汉化
 */
class HttpResultFunction<T : Any> : Function<Throwable, Observable<T>> {
    override fun apply(throwable: Throwable): Observable<T> {
        /**
         * 汉化后的异常信息说法
         */
        val throwableException: EventException= ExceptionEngine.handleException(throwable)

        /**
         * 发射错误信息回调
         */
        return Observable.error(throwableException)
    }
}
