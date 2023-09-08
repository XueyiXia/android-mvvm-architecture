package com.framework.mvvm.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:19
 * @说明: ViewModel的基类 使用ViewModel类
 */

open class BaseViewModel :ViewModel() {
    @SuppressLint("StaticFieldLeak")
    protected var context: Context?=null

}
