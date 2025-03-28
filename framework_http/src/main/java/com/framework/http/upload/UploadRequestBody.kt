package com.framework.http.upload

import com.framework.http.config.RxHttpConfigure
import com.framework.http.interfac.OnUpLoadFileListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.File
import java.io.IOException


/**
 * @author: xiaxueyi
 * @date: 2023-03-30
 * @time: 15:40
 * @说明: 上传RequestBody
 */
class UploadRequestBody(
    requestBody: RequestBody, //源文件
    private val file: File, //当前下标
    private val current: Int, //上传总文件
    private val totalFile: Int, uploadResult: OnUpLoadFileListener<Any>?
) : RequestBody() {
    //实际的待包装请求体
    private val requestBody: RequestBody

    //进度回调接口
    private val uploadResult: OnUpLoadFileListener<Any>?

    //包装完成的BufferedSink
    private var bufferedSink: BufferedSink? = null

    init {
        this.requestBody = requestBody
        this.uploadResult = uploadResult
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    /**
     * 重写writeTo
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
//        if (null == bufferedSink) {
//            bufferedSink = sink(sink).buffer()
//        }
        bufferedSink = sink(sink).buffer()
        bufferedSink?.let {
            requestBody.writeTo(it)
            //必须调用flush，否则最后一部分数据可能不会被写入
            it.flush()
        }

    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            var writtenBytesCount = 0L

            //总字节长度，避免多次调用contentLength()方法
            var totalBytesCount = 0L
            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                //增加当前写入的字节数
                writtenBytesCount += byteCount
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0L) {
                    totalBytesCount = contentLength()
                }
                //回调接口
                if (uploadResult != null) {
                    RxHttpConfigure.getInstance().getHandler()?.post {
                        val progress = writtenBytesCount.toFloat() / totalBytesCount.toFloat()
                        uploadResult.progress(
                            file,
                            writtenBytesCount,
                            totalBytesCount,
                            progress,
                            current,
                            totalFile
                        )
                    }
                }
            }
        }
    }
}