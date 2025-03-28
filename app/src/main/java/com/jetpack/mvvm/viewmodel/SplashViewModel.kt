package com.jetpack.mvvm.viewmodel

import androidx.core.graphics.toColorInt
import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.livedata.StringLiveData
import com.framework.mvvm.viewmodel.BaseViewModel


class SplashViewModel : BaseViewModel() {

    val bgColor = MutableLiveData<MutableList<Int>>().apply {
        "#F5B95F".toColorInt()
    }

    var titleData = StringLiveData().apply {
        value="测试"
    }

    var clickData= StringLiveData().apply {
        value="测试"
    }


}