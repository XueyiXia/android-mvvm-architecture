package com.framework.http.utils

import okio.Buffer
import java.io.EOFException


/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明: 如果所讨论的正文可能包含人类可读的文本，则返回 true。 使用一小部分代码点来检测二进制文件签名中常用的 unicode 控制字符。
 */
fun Buffer.isProbablyUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = size.coerceAtMost(64)
        copyTo(prefix, 0, byteCount)
        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (_: EOFException) {
        return false // Truncated UTF-8 sequence.
    }
}