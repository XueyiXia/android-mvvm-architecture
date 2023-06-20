package com.jetpack.mvvm

import androidx.lifecycle.ViewModelStore
import com.framework.mvvm.base.BaseAppLoader
import com.framework.mvvm.event.AppViewModel
import com.framework.mvvm.event.EventViewModel
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:46
 * @说明:
 */

class AppLoader :BaseAppLoader(ViewModelStore()) {


    companion object {
       private lateinit var mInstance: AppLoader
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel

        fun getInstance()=mInstance
    }


    override fun onCreate() {
        super.onCreate()
        mInstance = this
        eventViewModelInstance = getAppViewModelProvider()[EventViewModel::class.java]
        appViewModelInstance = getAppViewModelProvider()[AppViewModel::class.java]
    }
}