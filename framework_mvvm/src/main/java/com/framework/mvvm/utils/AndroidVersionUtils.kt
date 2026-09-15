package com.framework.mvvm.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.framework.mvvm.utils.AndroidVersionUtils.hasR
import com.framework.mvvm.utils.AndroidVersionUtils.hasS


/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:19
 * @说明: [hasR] 判断版本是否在R以上,[hasS] 判断版本是否在S以上
 */

object AndroidVersionUtils {

    /**
     * 判断当前手机系统版本API是否是24以上。
     * @return 24以上返回true，否则返回false。
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    fun hasNougat(): Boolean {
        return true
    }

    /**
     * 判断当前手机系统版本API是否是29以上。
     * @return 29以上返回true，否则返回false。
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun hasQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 判断当前手机系统版本API是否是30以上。
     * @return 30以上返回true，否则返回false。
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun hasR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }


    /**
     * 判断当前手机系统版本API是否是31以上。
     * @return 31以上返回true，否则返回false。
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun hasS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

}