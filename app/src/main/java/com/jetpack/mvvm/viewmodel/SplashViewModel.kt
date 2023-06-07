package com.jetpack.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.databind.StringObservableField
import com.framework.mvvm.viewmodel.BaseViewModel


class SplashViewModel : BaseViewModel() {

    var name = MutableLiveData<StringObservableField>()
}