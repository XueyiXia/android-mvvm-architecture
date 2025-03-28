package com.framework.http.utils

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * @author: xiaxueyi
 * @date: 2023-03-31
 * @time: 15:25
 * @说明:
 */
object StorageHelper {

    /**
     * 获取内部沙盒文件目录
     * /data/data/xxx/files
     *
     * @param context
     * @return
     */
    fun getInternalFilesPath(context: Context): String{
        return context.filesDir.absolutePath
    }

    /**
     * 获取内部沙盒缓存目录
     * /data/data/xxx/cache
     *
     * @param context
     * @return
     */
    fun getInternalCachePath(context: Context): String {
        return context.cacheDir.absolutePath
    }

    /**
     * 获取外部沙盒文件根目录
     * /storage/emulated/0/Android/data/xxx/files
     *
     * @param context
     * @return
     */
    fun getExternalSandBoxFilesPath(context: Context): String {
        return getExternalSandBoxPath(context, null)
    }

    fun getExternalSandBoxPath(context: Context, type: String?): String {
        return context.getExternalFilesDir(type)!!.absolutePath
    }

    /**
     * 获取外部沙盒缓存目录
     * /storage/emulated/0/Android/data/xxx/cache
     *
     * @param context
     * @return
     */
    fun getExternalSandBoxCachePath(context: Context): String {
        return context.externalCacheDir!!.absolutePath
    }

    /**
     * 外部公共目录
     *
     * @param type
     * @return
     */
    fun getExternalStoragePublicPath(type: String?): String {
        return Environment.getExternalStoragePublicDirectory(type).absolutePath
    }

    /**
     * 外部公共目录文件
     *
     * @param type
     * @return
     */
    fun getExternalStoragePublicDirectory(type: String?): File {
        return Environment.getExternalStoragePublicDirectory(type)
    }

    /**
     * 是否有外部存储(SD卡)
     * @return
     */
    fun hasExternalStorage(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}