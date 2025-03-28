package com.framework.http.function


import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit

class RetryWithDelayFunction(
    private val maxRetries: Int,
    private val retryDelayMillis: Long) : Function<Observable<Throwable>, ObservableSource<Any>> {


    private var retryCount = 0

    @Throws(Exception::class)
    override fun apply(throwableObservable: Observable<Throwable>): ObservableSource<Any> {
        return throwableObservable.flatMap { throwable ->
            if (++retryCount <= maxRetries) {
                Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS)
            } else Observable.error(throwable)
        }
    }

}