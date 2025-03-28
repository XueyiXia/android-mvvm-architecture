package com.framework.http.requestbody

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException
/**
 * @author: xiaxueyi
 * @date: 2023-03-30
 * @time: 15:40
 * @说明: 字节请求包
 */
class ByteArrayRequestBody(private val contentType: MediaType?, private val byteArray: ByteArray) : RequestBody() {
    override fun contentType(): MediaType? {
        return contentType
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return byteArray.size.toLong()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        sink.write(byteArray, 0, byteArray.size)
    }

}