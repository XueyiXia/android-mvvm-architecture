package com.framework.versionplugin


/**
 * @author: xiaxueyi
 * @date: 2022-09-29
 * @time: 11:21
 * @说明:  如果数量少的话，放在一个类里面就可以，如果数量多的话，可以拆分为多个类
 */
object Versions {
    const val appcompat = "1.5.0"
    const val kotlin = "1.3.72"
    const val recyclerview = "1.2.1"
    const val coreKtx = "1.9.0"
    const val constraintLayout="2.1.4"
    const val material = "1.6.1"

    const val lifecycle_viewmodel = "2.5.0"

    const val lifecycle_livedata = "2.5.0"
}

object AndroidX {
    const val coreKtx = "androidx.core:core-ktx:1.9.0"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
//
//    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.lifecycle_viewmodel}"
//
//    const val livedata = "androidx.lifecycle:lifecycle-livedata:${Versions.lifecycle_livedata}"
}


