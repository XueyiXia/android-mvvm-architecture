package com.jetpack.mvvm.viewmodel

import com.framework.mvvm.livedata.StringLiveData
import com.framework.mvvm.viewmodel.BaseViewModel


class SplashViewModel : BaseViewModel() {

    var titleData = StringLiveData()

    var clickData= StringLiveData()


}