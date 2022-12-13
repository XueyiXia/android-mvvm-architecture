package com.framework.mvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @author: xiaxueyi
 * @date: 2022-11-30
 * @time: 11:26
 * @说明: 自定义的String类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */

class StringLiveData : MutableLiveData<String>(){

    override fun getValue(): String {
        return super.getValue() ?: ""
    }
}