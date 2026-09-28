package com.jetpack.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.mvvm.WifiScanner
import com.jetpack.mvvm.bean.WifiInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddDeviceViewModel(
    private val scanner: WifiScanner
):ViewModel(){


    private val _wifiList= MutableStateFlow<List<WifiInfo>>(emptyList())
    val wifiList= _wifiList.asStateFlow()

    fun scanWifi(){
        viewModelScope.launch{
            val list= scanner.scan()
            _wifiList.value= list

        }
    }


}