package com.framework.http.interfac

import java.io.File

/**
 * @author: xiaxueyi
 * @date: 2023-03-31
 * @time: 12:55
 * @说明:
 */
open class OnUpLoadFileListener <T> : HttpResponseListener<T>() {

    /**
     * 上传进度回调
     *
     * @param currentSize  当前值
     * @param totalSize    总大小
     * @param progress     进度
     * @param currentIndex 当前下标
     * @param totalFile    总文件数
     */
    open fun progress(
        file: File?,
        currentSize: Long,
        totalSize: Long,
        progress: Float,
        currentIndex: Int,
        totalFile: Int
    ) {
    }

    override fun onNext(response: T?) {
        TODO("Not yet implemented")
    }

    override fun onError(e: Throwable?) {
        TODO("Not yet implemented")
    }
}