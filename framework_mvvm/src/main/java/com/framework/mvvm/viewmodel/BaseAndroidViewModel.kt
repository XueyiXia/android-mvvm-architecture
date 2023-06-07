package com.framework.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:19
 * @说明: ViewModel的基类 使用ViewModel类
 */

abstract class BaseAndroidViewModel<BaseData, Data, Key> : ViewModel() {

    val dataList = ArrayList<Data>()

    private val pageLiveData = MutableLiveData<Key>()

//    val dataLiveData = Transformations.switchMap(pageLiveData) { page ->
//        getData(page)
//    }

    abstract fun getData(page: Key): LiveData<Result<BaseData>>

//    fun getDataList(page: Key) {
//        pageLiveData.value = page!!
//    }

}