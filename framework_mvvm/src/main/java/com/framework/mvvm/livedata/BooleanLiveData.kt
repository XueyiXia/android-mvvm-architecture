package com.framework.mvvm.livedata

import androidx.lifecycle.MutableLiveData


/**
 * @author: xiaxueyi
 * @date: 2022-12-16
 * @time: 13:22
 * @说明: 自定义的Boolean类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}

