package com.jetpack.mvvm.di

import com.jetpack.mvvm.ble.BleManager
import com.jetpack.mvvm.ble.repository.BleRepository
import com.jetpack.mvvm.viewmodel.DeviceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val bleModule = module {


    single {
        BleManager(
            context = get()
        )
    }

    single {
        BleRepository(
            bleManager = get()
        )
    }

    viewModel {
        DeviceViewModel(
            repository = get()
        )
    }
}