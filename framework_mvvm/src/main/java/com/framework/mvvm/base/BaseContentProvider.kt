package com.framework.mvvm.base

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.ProcessLifecycleOwner
import com.framework.mvvm.lifecycle.KotlinAppLifeObserver
import com.framework.mvvm.lifecycle.KotlinLifeCycleCallBack


/**
 * @author: xiaxueyi
 * @date: 2023-05-01
 * @time: 15:55
 * @说明:内容提供者
 */


val appContext: Application by lazy { KotlinContentProvider.app }

class KotlinContentProvider : ContentProvider() {

    companion object {
        lateinit var app: Application
        var mActivityLife = true  //app 生命周期
        var mAppLife = true
    }

    override fun onCreate(): Boolean {
        val application = context!!.applicationContext as Application
        install(application)
        return true
    }

    private fun install(application: Application) {
        app = application
        if (mActivityLife) application.registerActivityLifecycleCallbacks(KotlinLifeCycleCallBack())
        if (mAppLife) ProcessLifecycleOwner.get().lifecycle.addObserver(KotlinAppLifeObserver)
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}