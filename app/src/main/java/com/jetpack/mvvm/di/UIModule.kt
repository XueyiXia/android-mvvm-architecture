package com.jetpack.mvvm.di

import com.jetpack.mvvm.MqttManager
import com.jetpack.mvvm.WifiScanner
import com.jetpack.mvvm.ble.BleManager
import com.jetpack.mvvm.repository.ble.BleRepository
import com.jetpack.mvvm.viewmodel.DeviceViewModel
import com.jetpack.mvvm.repository.wifi.WifiRepository
import com.jetpack.mvvm.viewmodel.AddDeviceViewModel
import com.jetpack.mvvm.viewmodel.DeviceBindViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val bleModule = module {


    single {
        BleManager(
            context = get()
        )
    }


    single {
        MqttManager(
            context = get()
        )
    }

    single {
        BleRepository(
            bleManager = get(),get()
        )
    }

    single {
        WifiRepository()
    }


    viewModel {
        DeviceViewModel(
            repository = get(),get()
        )
    }
}



val scanWifiModule = module {


    single {
        WifiScanner(get())
    }


    viewModel {
        AddDeviceViewModel(
            scanner = get(),get()
        )
    }
}


val bindingDeviceModule = module {



    viewModel {
        DeviceBindViewModel(
             get()
        )
    }
}