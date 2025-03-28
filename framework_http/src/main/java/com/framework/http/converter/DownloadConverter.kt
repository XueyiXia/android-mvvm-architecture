package com.framework.http.converter


import com.framework.http.bean.DownloadInfo
import com.framework.http.callback.DownloadCallback
import com.framework.http.callback.ProgressCallback
import com.framework.http.config.DownloadConfigure
import com.framework.http.requestbody.ProgressResponseBody
import com.framework.http.utils.Platform
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type

class DownloadConverter<T>(private val downloadRequest: DownloadConfigure, private val callback: DownloadCallback<T>) {

    private val downloadInfo= DownloadInfo(downloadRequest.urlLink, downloadRequest.directoryFile, downloadRequest.filename)

    @Throws(Exception::class)
    fun convert(body: ResponseBody, type: Type): T {
        try {
            val fileDir = File(downloadInfo.dir)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            val file = File(downloadInfo.dir, downloadInfo.fileName)
            val currentProgress= if (downloadRequest.isBreakpoint) {
                if (file.exists()) {
                    file.length()
                } else {
                    0L
                }
            } else {
                0L
            }
            val fos = FileOutputStream(file, downloadRequest.isBreakpoint)

            val progressResponseBody = ProgressResponseBody(body, object : ProgressCallback {
                override fun onProgress(readBytes: Long, totalBytes: Long) {
                    downloadInfo.progress=currentProgress+readBytes
                    Platform.get().defaultCallbackExecutor()?.execute {
                        callback.onProgress(downloadInfo.progress,
                            currentProgress+totalBytes) }
                }
            })
            downloadInfo.total = currentProgress + progressResponseBody.contentLength()

            Platform.get().defaultCallbackExecutor()?.execute {
                callback.onProgress(currentProgress,downloadInfo.total)
            }

            val sink = fos.sink().buffer()

            sink.writeAll(progressResponseBody.source())
            sink.flush()
            sink.close()
            progressResponseBody.close()
        } catch (e: Exception) {
            throw e
        }
        return downloadInfo as T
    }

}