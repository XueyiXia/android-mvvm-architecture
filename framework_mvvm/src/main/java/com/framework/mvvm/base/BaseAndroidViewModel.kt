package com.framework.mvvm.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseAndroidViewModel<BaseData, Data, Key> : ViewModel() {

    val dataList = ArrayList<Data>()

    private val pageLiveData = MutableLiveData<Key>()

//    val dataLiveData = Transformations.switchMap(pageLiveData) { page ->
//        getData(page)
//    }

    abstract fun getData(page: Key): LiveData<Result<BaseData>>

//    fun getDataList(page: Key) {
//        pageLiveData.value = page
//    }

}