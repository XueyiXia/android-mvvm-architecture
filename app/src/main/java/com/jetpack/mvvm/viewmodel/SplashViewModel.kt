package com.jetpack.mvvm.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.livedata.StringLiveData
import com.framework.mvvm.viewmodel.BaseViewModel
import androidx.core.graphics.toColorInt


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