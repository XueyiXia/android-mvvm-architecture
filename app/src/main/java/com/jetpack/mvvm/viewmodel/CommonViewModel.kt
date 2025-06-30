package com.jetpack.mvvm.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds


class CommonViewModel : BaseViewModel() {

    val bgColor = MutableLiveData<Int>().apply { this.value=Color.parseColor("#958ddd")}


}