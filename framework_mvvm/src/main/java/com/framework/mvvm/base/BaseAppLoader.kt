package com.framework.mvvm.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author: xiaxueyi
 * @date: 2023-06-20
 * @time: 10:23
 * @说明: 在Activity/fragment中获取Application级别的ViewModel
 */

open class BaseAppLoader(override val viewModelStore: ViewModelStore) : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null


    /**
     *
     * @return ViewModelStore
     */
    fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
    }


    /**
     * 获取一个全局的ViewModel
     * @return ViewModelProvider
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    /**
     *
     * @return ViewModelProvider.Factory
     */
    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}