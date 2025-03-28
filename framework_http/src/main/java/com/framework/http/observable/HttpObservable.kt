package com.framework.http.observable

import com.framework.http.function.HttpResultFunction
import com.framework.http.function.OnDisposeAction
import com.framework.http.function.RetryWithDelayFunction
import com.framework.http.observer.HttpObserver
import com.framework.http.scheduler.SchedulerUtils
import io.reactivex.rxjava3.core.Observable


/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:57
 * @说明:   网络请求Observable(被监听者)
 * * 调度顺序
 * 1.map()
 * 2.onErrorResumeNext()
 * 3.doOnDispose()
 * 4.observe()
 */

class HttpObservable  constructor(private val apiObservable: Observable<Any>?, private val httpObserver: HttpObserver<Any>?){

    companion object{
        var maxRetries = 5
        var retryDelayMillis = 100L
    }


    /**
     *  Map 操作符，被观察数据再次进行处理，生成新的被观察者，发送到观察者中进行处理，即在HttpObserver回调
     * @return Observable<Any>?
     */
    private fun map(): Observable<Any>? {
        return apiObservable?.map { t ->
            t.toString()
//            GsonBuilder().disableHtmlEscaping().create().toJson(t.toString())
        }
    }

    /**
     *
     * @return Observable<Any> ?
     */
    private fun onErrorResumeNext(): Observable<Any> ?{
        val httpResultFunction= HttpResultFunction<Any>()
        return map()?.onErrorResumeNext(httpResultFunction)
    }


    /**
     * 订阅
     * @return Observable<Any>?
     */
    private fun doOnDispose(): Observable<Any>? {
        val onDisposeAction=OnDisposeAction(httpObserver)
        return onErrorResumeNext()?.doOnDispose(onDisposeAction)
    }

    /**
     * 被观察者和观察者订阅
     */
    fun observe() {
        if (httpObserver != null) {
            val retryWithDelayFunction=RetryWithDelayFunction(maxRetries,retryDelayMillis)
            doOnDispose()
                ?.retryWhen(retryWithDelayFunction)
                ?.compose(SchedulerUtils.ioToMainScheduler())
                ?.subscribe(httpObserver)
        }
    }
}