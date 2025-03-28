package com.jetpack.mvvm.di

import com.jetpack.mvvm.repository.NetworkRepository
import org.koin.dsl.module

val networkModule = module {
    single {
        NetworkRepository(get())
    }
}
