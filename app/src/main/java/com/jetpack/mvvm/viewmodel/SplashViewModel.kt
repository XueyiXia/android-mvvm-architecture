package com.jetpack.mvvm.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.livedata.StringLiveData
import com.framework.mvvm.viewmodel.BaseViewModel


class SplashViewModel : BaseViewModel() {

    val bgColor = MutableLiveData<MutableList<Int>>().apply {
        Color.parseColor("#F5B95F")
    }

    var titleData = StringLiveData()

    var clickData= StringLiveData()


}