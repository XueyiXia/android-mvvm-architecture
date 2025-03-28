package com.framework.http.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Method


/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明:
 */
object StringUtils {


    /**
     * 从Assets读取JSON文件
     * @param context
     * @param fileName
     * @return
     */
    fun getAssetsFile(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(
                        fileName!!
                    )
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


    /**
     * 获取Raw文件数据
     * @param context
     * @param fileName
     * @return
     */
    fun getRawFile(context: Context, fileName: Int): String {
        val stringBuilder = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(fileName)
            val isr = InputStreamReader(inputStream, "UTF-8")
            val br = BufferedReader(isr)
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


    /**
     * 获取基类的URL
     * @param url
     * @return
     */
    fun getBasUrl(url: String): String {
        var url = url
        var head = ""
        if (!TextUtils.isEmpty(url)) {
            var index = url.indexOf("://")
            if (index != -1) {
                head = url.substring(0, index + 3)
                url = url.substring(index + 3)
            }
            index = url.indexOf("/")
            if (index != -1) {
                url = url.substring(0, index + 1)
            }
            return head + url
        }
        return ""
    }


    /**
     * 删除文本最后一个逗号
     * @param str String
     * @return String
     */
    fun removeLastComma(str: String): String {
        var string =""
        if(TextUtils.isEmpty(str)){
            return string
        }
        string= str.substring(0,str.lastIndexOf(","))  //删除逗号
        return string
    }


    /**
     * 禁掉系统软键盘
     * @param context Context
     */
    fun hideSoftInputMethod(context: Context,editText: EditText){
        if(context is Activity){
            var activity =(context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val currentVersion = Build.VERSION.SDK_INT
            var methodName: String? = null
            if (currentVersion >= 16) {
                // 4.2
                methodName = "setShowSoftInputOnFocus"
            } else if (currentVersion >= 14) {
                // 4.0
                methodName = "setSoftInputShownOnFocus"
            }
            if (methodName == null) {
                editText.inputType = InputType.TYPE_NULL
            } else {
                val cls: Class<EditText> = EditText::class.java
                val setShowSoftInputOnFocus: Method
                try {
                    setShowSoftInputOnFocus = cls.getMethod(methodName, Boolean::class.javaPrimitiveType)
                    setShowSoftInputOnFocus.isAccessible = true
                    setShowSoftInputOnFocus.invoke(editText, false)
                } catch (e: Exception) {
                    editText.inputType = InputType.TYPE_NULL
                    e.printStackTrace()
                }
            }
        }

    }


    /**
     * 禁掉系统软键盘
     * @receiver View
     */
    fun View.hideSystemInputMethod(){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken,0)
    }



    /**
     * 安装Apk
     */
    fun installApk(context: Context, file: File?, authority: String) {
        if (file == null || !file.exists()) {
            return
        }
        try {
//            val pm = context.packageManager
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (!pm.canRequestPackageInstalls()) {
//                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.packageName))
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(intent)
//                    return
//                }
//            }
            val intent = Intent(Intent.ACTION_VIEW)
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                FileProvider.getUriForFile(context, authority, file)
            } else {
                Uri.fromFile(file)
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 获取下载文件的范围
     * @param position Long
     * @return String
     */
    fun getDownloadRange(position:Long):String{
       return String.format("bytes=%d-", position)
    }
}