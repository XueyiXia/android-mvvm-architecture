package com.framework.http.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings

/**
 * @author: xiaxueyi
 * @date: 2023-03-31
 * @time: 15:30
 * @说明:
 */
object IntentHelper {


    //跳转到指定应用
    fun launchApp(context: Context, packageName: String?): Boolean {
        try {
            context.startActivity(context.packageManager.getLaunchIntentForPackage(packageName!!))
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //跳转到指定应用
    fun launchAppWithScheme(context: Context, scheme: String?): Boolean {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(scheme)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //跳转到应用详情
    fun gotoAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    //跳转到应用程序
    fun gotoApplicationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    //跳转到WLAN设置
    fun gotoWifiSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    //跳转到系统设置
    fun gotoSettings(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    //跳转到开发者选项
    fun gotoDevelopmentSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    //跳转到位置设置
    fun gotoLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    //从相册中选择照片
    @SuppressLint("IntentReset")
    fun gotoSelectPhoto(activity: Activity, requestCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode)
    }
}