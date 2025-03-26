package com.framework.mvvm.livedata

import androidx.lifecycle.MutableLiveData


/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 13:22
 * @说明: 自定义的Any类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class ListLiveData<T> : MutableLiveData<List<T>>() {

    override fun getValue(): List<T> {
        return super.getValue() ?: mutableListOf()
    }
}

