package com.framework.http.callback

interface ProgressCallback {
    fun onProgress(readBytes: Long, totalBytes: Long)
}