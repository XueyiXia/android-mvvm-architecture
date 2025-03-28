package com.framework.http.callback

import com.framework.http.interfac.HttpResponseListener
 open class DownloadCallback<T> : HttpResponseListener<T>(){
     override fun onNext(response: T?) {
         TODO("Not yet implemented")
     }

     override fun onError(e: Throwable?) {
         TODO("Not yet implemented")
     }

 }