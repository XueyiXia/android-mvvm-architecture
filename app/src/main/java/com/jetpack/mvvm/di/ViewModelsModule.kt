package com.jetpack.mvvm.di


import com.jetpack.mvvm.ui.home.HomeViewModel
import com.jetpack.mvvm.ui.news.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { HomeViewModel() }
    viewModel { NewsViewModel() }
}
