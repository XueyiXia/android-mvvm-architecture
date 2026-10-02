package com.jetpack.mvvm.bean


data class WifiStatus(
    val type:String,
    val status:String,
    val ip:String?=null
)