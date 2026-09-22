package com.jetpack.mvvm

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.jetpack.mvvm.di.bleModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

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

        configureKoin()
    }


    /**
     * 設定 Koin 依賴注入框架
     * 初始化所有必要的模組和依賴
     */
    private fun configureKoin() {
        startKoin {
            androidContext(this@AppLoader)
            modules(getKoinModules())
        }
    }

    private fun getKoinModules(): MutableList<Module> {
        val modules = mutableListOf<Module>()
        modules.add(bleModule)
        return modules
    }
}