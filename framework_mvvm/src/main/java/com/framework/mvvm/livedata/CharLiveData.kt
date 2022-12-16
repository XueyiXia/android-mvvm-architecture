package com.framework.mvvm.livedata

import androidx.lifecycle.MutableLiveData


/**
 * @author: xiaxueyi
 * @date: 2022-12-16
 * @time: 13:22
 * @说明: 自定义的Char类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class CharLiveData : MutableLiveData<Char>() {
    override fun getValue(): Char {
        return super.getValue()?:'\u0000'
    }
}