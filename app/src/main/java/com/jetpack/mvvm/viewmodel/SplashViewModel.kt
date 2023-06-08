package com.jetpack.mvvm.viewmodel

import android.graphics.Color
import com.framework.mvvm.databind.IntObservableField
import com.framework.mvvm.databind.StringObservableField
import com.framework.mvvm.viewmodel.BaseViewModel


class SplashViewModel : BaseViewModel() {

    var name = StringObservableField()

    var bgColor = IntObservableField(Color.GRAY)

}