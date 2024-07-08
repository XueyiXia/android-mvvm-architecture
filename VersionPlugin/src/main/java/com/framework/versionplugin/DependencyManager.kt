package com.framework.versionplugin

import com.framework.versionplugin.Versions.agp
import com.framework.versionplugin.Versions.kotlin


/**
 * @author: xiaxueyi
 * @date: 2022-09-29
 * @time: 11:21
 * @说明:  如果数量少的话，放在一个类里面就可以，如果数量多的话，可以拆分为多个类
 */
object Versions {
    const val appcompat = "1.5.0"
    const val recyclerview = "1.2.1"
    const val coreKtx = "1.9.0"
    const val constraintLayout="2.1.4"
    const val material = "1.6.1"
    const val lifecycleViewmodel = "2.8.3"
    const val lifecycleLivedata = "2.8.3"

    const val agp = "8.5.0"
    const val kotlin = "1.8.0"
}

object AndroidX {
    const val coreKtx = "androidx.core:core-ktx:1.9.0"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    //lifecycle
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.8.3"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:2.8.3"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    // viewModel
    const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewmodel}"
    const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLivedata}"

    const val androidxfragmentKtx =  "androidx.fragment:fragment-ktx:1.8.1"
    // unpeopleLivedata
    const val unpeopleLivedata =  "com.kunminx.archi:unpeople-livedata:4.4.1-beta1"
    //navigation
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.7.7"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:2.7.7"

    const val preferenceKtx = "androidx.preference:preference-ktx:1.2.1"
    const val supportMultidex = "com.android.support:multidex:1.0.3"
    const val startupruntime= "androidx.startup:startup-runtime:1.0.0"
}

object AndroidXPlugins{
    const val comAndroidApplication = agp;
    const val comandroidlibrary = agp
    const val orgjetbrainskotlinandroid = kotlin
}


