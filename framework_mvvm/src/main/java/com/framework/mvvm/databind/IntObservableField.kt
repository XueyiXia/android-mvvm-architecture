package com.framework.mvvm.databind

import androidx.databinding.ObservableField

/**
 * @author: xiaxueyi
 * @date: 2022-12-16
 * @time: 13:50
 * @说明: 自定义的Int类型 ObservableField  提供了默认值，避免取值的时候还要判空
 */
class IntObservableField(value: Int = 0) : ObservableField<Int>(value) {

    override fun get(): Int {
        return super.get()!!
    }

}