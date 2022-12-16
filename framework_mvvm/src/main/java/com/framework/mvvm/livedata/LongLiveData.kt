package com.framework.mvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @author: xiaxueyi
 * @date: 2022-12-16
 * @time: 13:22
 * @说明: 自定义的Long类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */

class LongLiveData :MutableLiveData<Long> (){

    override fun getValue(): Long {
        return super.getValue()?:0
    }
}