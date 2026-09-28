package com.jetpack.mvvm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.jetpack.mvvm.bean.WifiInfo

class WifiScanner(
    private val context: Context
){

    private val wifiManager=
        context.applicationContext
            .getSystemService(
                Context.WIFI_SERVICE
            ) as WifiManager


    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun scan():List<WifiInfo>{

        if(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            return emptyList()
        }

        return wifiManager.scanResults
            .filter{
                it.SSID.isNotEmpty()
            }
            .map{
                WifiInfo(
                    it.SSID,
                    it.level
                )

            }

    }

}