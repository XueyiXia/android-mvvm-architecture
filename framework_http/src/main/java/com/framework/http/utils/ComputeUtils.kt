package com.framework.http.utils

import android.text.TextUtils
import java.io.File

/**
 * 计算工具类
 *
 */
object ComputeUtils {
    /**
     * 计算进度值
     *
     * @param current
     * @param total
     * @return
     */
    fun getProgress(current: Long, total: Long): Float {
        return current.toFloat() / total.toFloat()
    }

    /**
     * 文件是否存在
     *
     * @param fileUrl 文件路径
     * @return
     */
    fun isFileExists(fileUrl: String?): Boolean {
        var flag = false
        val file = File(fileUrl)
        if (file.exists() && file.isFile) {
            flag = true
        }
        return flag
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    fun deleteFile(filePath: String?): Boolean {
        if (TextUtils.isEmpty(filePath)){
            return false
        }
        val file = File(filePath)
        return if (file.exists() && file.isFile) {
            if (file.delete()) {
                println("删除单个文件" + filePath + "成功！")
                true
            } else {
                println("删除单个文件" + filePath + "失败！")
                false
            }
        } else {
            println("删除单个文件失败：" + filePath + "不存在！")
            false
        }
    }
}