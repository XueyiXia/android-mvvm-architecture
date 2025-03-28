package com.framework.http.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.framework.http.api.BaseResponse
import com.framework.http.bean.DownloadInfo
import com.framework.http.callback.DownloadCallback
import com.framework.http.config.DownloadConfigure
import com.framework.http.http.RxHttp
import com.framework.http.interfac.OnUpLoadFileListener
import com.framework.http.utils.HttpConstants
import java.io.File
import java.util.*

/**
 * @author: xiaxueyi
 * @date: 2023-04-20
 * @time: 10:11
 * @说明: MVVM架构网络仓库,UI->ViewModel->Repository->LiveData(ViewModel)->UI
 */
class NetworkRepository private constructor() {

    companion object {
        fun getInstance() = NetworkRepository.holder
    }

    private object NetworkRepository {
        val holder = NetworkRepository()
    }


    /**
     *
     * @param context Context
     * @param url String
     * @param params Map<String, Any?>?
     * @param fileList MutableList<File>
     * @param tag Any?
     * @param onUpLoadFileListener OnUpLoadFileListener<BaseResponse<T>>
     */
    inline fun <reified T> httpUpload(
        context: Context,
        url: String,
        params: TreeMap<String, Any>?,
        fileList: MutableList<File>,
        tag: Any? = null,  onUpLoadFileListener: OnUpLoadFileListener<BaseResponse<T>>
    ){

        RxHttp.getInstance()
            .setContext(context)
            .setApiUrl(url)
            .setFile(HttpConstants.UPLOAD_KEY_FILE,fileList)
            .setParameter(params!!)
            .setTag(tag as String?)
            .build()
            .execute(onUpLoadFileListener)
    }


    /**
     * 下载
     * context:上下文,如不需要和生命周期绑定,应该传递applicationContext
     * url:下载地址
     * dir:本地目录路径
     * filename:保存文件名称
     * callback:下载进度回调
     * md5:下载文件的MD5值
     * breakpoint:是否支持断点下载,默认为true
     */
    fun httpDownload(
        context: Context,
        url: String,
        dir: String,
        filename: String,
        callback: DownloadCallback<DownloadInfo>,
        md5: String? = null,
        breakPoint: Boolean = false,
        tag: Any? = null) {

        val downloadConfigure: DownloadConfigure = DownloadConfigure.get()
        downloadConfigure.setDirectoryFile(dir)
        downloadConfigure.setFileName(filename)
        downloadConfigure.setDownloadUrl(url)
        downloadConfigure.setMd5(md5)
        downloadConfigure.isBreakpoint=breakPoint

        RxHttp.getInstance()
            .setDownloadConfigure(downloadConfigure)
            .setContext(context)
            .setTag(tag as String?)
            .build()
            .execute(callback)
    }

}