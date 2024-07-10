package com.jetpack.mvvm

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:46
 * @说明:
 */

class AppLoader : Application() {


    /**
     * +++++++++++++++++++++++多dex模式测试-开始+++++++++++++++++++++
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun onCreate() {
        super.onCreate()
    }
}