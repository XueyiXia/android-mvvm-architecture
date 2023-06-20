package com.jetpack.mvvm.viewmodel

import com.framework.mvvm.databind.StringObservableField
import com.framework.mvvm.livedata.StringLiveData
import com.framework.mvvm.viewmodel.BaseViewModel


class SplashViewModel : BaseViewModel() {

    var data = StringLiveData()

    var title = StringObservableField("Test Data")

}