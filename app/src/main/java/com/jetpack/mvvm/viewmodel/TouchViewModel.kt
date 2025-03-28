package com.jetpack.mvvm.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.viewmodel.BaseViewModel


class TouchViewModel : BaseViewModel() {

    val bgColor = MutableLiveData<Int>().apply { this.value=Color.parseColor("#958ddd")}


}