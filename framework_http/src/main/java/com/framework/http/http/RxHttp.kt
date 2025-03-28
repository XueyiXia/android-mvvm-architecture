package com.framework.http.http

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.framework.http.api.APIService
import com.framework.http.bean.DownloadInfo
import com.framework.http.callback.DownloadCallback
import com.framework.http.config.DownloadConfigure
import com.framework.http.config.RxHttpBuilder
import com.framework.http.config.RxHttpConfigure
import com.framework.http.converter.DownloadConverter
import com.framework.http.enum.HttpMethod
import com.framework.http.function.RetryWithDelayFunction
import com.framework.http.interfac.OnUpLoadFileListener
import com.framework.http.interfac.SimpleResponseListener
import com.framework.http.manager.RetrofitManagerUtils
import com.framework.http.observable.HttpObservable
import com.framework.http.observable.HttpObservable.Companion.maxRetries
import com.framework.http.observable.HttpObservable.Companion.retryDelayMillis
import com.framework.http.observer.HttpObserver
import com.framework.http.scheduler.SchedulerUtils
import com.framework.http.upload.UploadRequestBody
import com.framework.http.utils.HttpConstants
import com.framework.http.utils.Md5Utils
import com.framework.http.utils.RequestUtils
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:41
 * @说明:
 */

open class RxHttp constructor(builder: RxHttpBuilder){


    companion object {
        fun getInstance(): RxHttpBuilder {
            return RxHttpBuilder()
        }
    }
    private var mContext: Context? = null

    private var method: HttpMethod? = null //请求方式

    private var header: MutableMap<String, Any> = TreeMap<String,Any>()

    private var parameter: MutableMap<String, Any> = TreeMap<String,Any>()

    private var lifecycleOwner: LifecycleOwner?=null //LifecycleOwner 生命周期

    private var tag: String? = null //标识请求的TAG

    private var fileMap: MutableMap<String, File>? = null //文件map

    private var baseUrl: String? = null //基础URL

    private var apiUrl: String? = null //apiUrl

    private var bodyString: String? = null //String参数

    private var isJson = false //是否强制JSON格式

    private var timeout: Long = 0 //超时时长

    private var timeUnit: TimeUnit? = null //时间单位

    private var httpObserver: HttpObserver<Any>? = null

    private var mSimpleResponseListener: SimpleResponseListener<Any>?=null //网络回调监听

    private var mOnUpLoadFileListener: OnUpLoadFileListener<Any>? = null

    private var mDownloadCallback: DownloadCallback<Any>? = null

    private var isBreakpoint = false //是否断点下载，true

    private var downloadConfigure: DownloadConfigure?=null

    /**
     * 初始化函数
     */
    init {
        mContext=builder.getContext()
        parameter = builder.getParameter()
        header = builder.getHeader()
        tag = builder.getTag()
        fileMap = builder.getFile()
        baseUrl = builder.getBaseUrl()
        apiUrl = builder.getApiUrl()
        isJson = builder.getIsJson()
        bodyString = builder.getBodyString()
        method = builder.getMethod()
        timeout = builder.getTimeOut()
        timeUnit = builder.getTimeUnit()
        lifecycleOwner = builder.getLifecycleOwner()
        downloadConfigure=builder.getDownloadConfigure()
    }

    /**
     * 执行网络请求
     * @param simpleResponseListener SimpleResponseListener<T>?
     */
    open fun <T> execute(simpleResponseListener: SimpleResponseListener<T>?) {
        if (simpleResponseListener == null) {
            throw NullPointerException("SimpleResponseListener must not null!")
        } else {
            this.mSimpleResponseListener=simpleResponseListener as SimpleResponseListener<Any>
            doRequest()
        }
    }

    /**
     * 执行普通Http请求 上传文件
     * @param uploadCallback OnUpLoadFileListener<T>?
     */
    open fun <T> execute(uploadCallback: OnUpLoadFileListener<T>?) {
        if (uploadCallback == null) {
            throw java.lang.NullPointerException("UploadCallback must not null!")
        } else {
            mOnUpLoadFileListener = uploadCallback as OnUpLoadFileListener<Any>
            doUpload()
        }
    }

    /**
     *  执行普通Http请求 下载文件
     * @param downloadCallback DownloadCallback<T>?
     */
    open fun <T> execute(downloadCallback: DownloadCallback<T>?) {
        if (downloadCallback == null) {
            throw java.lang.NullPointerException("DownloadCallback must not null!")
        } else {
            mDownloadCallback = downloadCallback as DownloadCallback<Any>
            doDownload()
        }
    }

    /**
     * 执行请求
     */
    private fun doRequest() {

        /**
         * 请求头处理
         */
        disposeHeader()

        /**
         * 请求参数处理
         */
        disposeParameter()


        /**
         * 请求方式处理（被观察）
         */
        val apiObservable : Observable<Any> = disposeApiObservable()

        /**
         * 构造 观察者
         */
        httpObserver = HttpObserver(mSimpleResponseListener,tag, lifecycleOwner)

        /**
         * 被观察者和观察者订阅
         */
        val httpObservable = HttpObservable(apiObservable, httpObserver)

        /**
         * 设置监听，被观察和观察者订阅
         */
        httpObservable.observe()

    }


    /**
     * 执行文件上传
     */
    private fun doUpload() {
        /**
         * 请求头处理
         */
        disposeHeader()

        /**
         * 请求参数处理
         */
        disposeParameter()

        /**
         * 处理文件集合
         */
       val fileList = disposeFileUpLoad()

        /**
         * 请求方式处理（被观察）
         */
        val apiObservable : Observable<Any> = getApiService().upload(disposeApiUrl(), parameter, header, fileList) as Observable<Any>

        /**
         * 构造 观察者
         */
        httpObserver = HttpObserver(mOnUpLoadFileListener,tag, lifecycleOwner)

        /**
         * 被观察者和观察者订阅
         */
        val httpObservable = HttpObservable(apiObservable, httpObserver)

        /**
         * 设置监听，被观察和观察者订阅
         */
        httpObservable.observe()
    }


    /**
     * 下载
     */

    @SuppressLint("CheckResult", "SuspiciousIndentation")
    private fun doDownload(){
        val dir=downloadConfigure?.directoryFile!!
        val fileName= downloadConfigure?.filename!!
        val downloadUrl=downloadConfigure?.urlLink!!
        val file = File(dir, fileName)

        val downloadInfo = DownloadInfo(downloadUrl,dir, fileName)

        if (!TextUtils.isEmpty(downloadConfigure?.md5)) {
            if (file.exists()) {
                val fileMd5 = Md5Utils.getMD5(file)
                if (downloadConfigure?.md5.equals(fileMd5, ignoreCase = true)) {
                    downloadInfo.total = file.length()
                    downloadInfo.progress = file.length()
                    mDownloadCallback?.onNext(downloadInfo)
                    mDownloadCallback?.onCompleted()
                    return
                }
            }
        }

        /**
         * 请求方式处理（被观察）
         */
        val apiService=getApiService()

        var apiObservable : Observable<ResponseBody>?=null

        if (isBreakpoint) {
            apiObservable = Observable.just(downloadUrl)
                .flatMap { url ->
                    val contentLength = getContentLength(url,apiService)
                    var startPosition: Long = 0
                    if (file.exists()) {
                        if (contentLength == -1L || file.length() >= contentLength) {
                            file.delete()
                        } else {
                            startPosition = file.length()
                        }
                    }
                    Observable.just(startPosition)

                }.flatMap { position -> apiService.download(downloadUrl, String.format("bytes=%d-", position))
                }.subscribeOn(Schedulers.io())

        } else {
            apiObservable = apiService.download(downloadUrl)
        }

        val observableFinal = apiObservable?.map{
            if(it is ResponseBody){
                val downloadConverter: DownloadConverter<out Any?> = DownloadConverter(downloadConfigure!!, mDownloadCallback as DownloadCallback<*>)
                    downloadConverter.convert(it, (mDownloadCallback as DownloadCallback<*>).type)
            }
        }

            ?.retryWhen(RetryWithDelayFunction(maxRetries,retryDelayMillis))
            ?.compose(SchedulerUtils.ioToMainScheduler())

        if (mContext is LifecycleOwner) {
            lifecycleOwner=mContext as LifecycleOwner
        }
        httpObserver = HttpObserver(mDownloadCallback,tag, lifecycleOwner)
        httpObserver?.let { observableFinal?.subscribeWith(it) }
    }



    /**
     * 获取API 服务
     * @return APIService
     */
    private fun getApiService(): APIService {
        val retrofit = RetrofitManagerUtils.getInstance().getRetrofit(getBaseUrl()!!)
        return retrofit.create(APIService::class.java)
    }

    /**
     * 接口的被观察者
     * @return Observable<Any>
     */
    private fun disposeApiObservable(): Observable<Any>{
        /**
         * 是否JSON格式提交参数
         */
        val hasBodyString = !TextUtils.isEmpty(bodyString)
        var requestBody: RequestBody? = null
        if (hasBodyString) {
            val mediaType : String = if (isJson) {
                HttpConstants.MIME_TYPE_JSON
            }else{
                HttpConstants.MIME_TYPE_TEXT
            }
            requestBody = bodyString?.toRequestBody(mediaType.toMediaType())
        }

        val apiService=getApiService()

        var apiObservable: Observable<JsonElement>? = null

        when (method) {
            HttpMethod.GET ->{
                apiObservable = apiService.get(disposeApiUrl(), parameter, header)
            }

            HttpMethod.POST ->{
                if (hasBodyString){
                    requestBody?.let {
                        apiObservable = apiService.post(disposeApiUrl(), it, header)
                    }
                } else{
                    apiObservable = apiService.post(disposeApiUrl(), parameter, header)
                }
            }

            HttpMethod.DELETE -> {
                apiObservable = if (parameter.isNotEmpty()) {
                    apiService.delete(disposeApiUrl(), parameter, header)
                } else {
                    apiService.delete(disposeApiUrl())
                }

            }

            HttpMethod.PUT -> {
                apiObservable = apiService.put(disposeApiUrl(), parameter, header)
            }

            else -> { //默认get 请求
                apiObservable = apiService.get(disposeApiUrl(), parameter, header)
            }
        }

        return apiObservable as Observable<Any>
    }


    /**
     * 处理请求头
     */
    private fun disposeHeader(){
        //处理header中文或者换行符出错问题
        for (key in header.keys) {
            header[key]= RequestUtils.getHeaderValueEncoded(header[key])
        }
    }


    /**
     * 处理请求参数
     */
    private fun disposeParameter(){
        try {
            //添加基础 Parameter
            RxHttpConfigure.getInstance().getBaseParameter()?.let {
                if (it.isNotEmpty()) {
                    parameter.putAll(it)
                }
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

    }


    /**
     * 处理文件集合
     */
    private fun disposeFileUpLoad(): List<MultipartBody.Part>{
        val fileList: MutableList<MultipartBody.Part> = ArrayList()
        fileMap?.let {
            val size = it.size
            var index = 1
            var file: File?
            var requestBody: RequestBody

            for (key in it.keys) {
                file = it[key]
                val mediaType=HttpConstants.MIME_TYPE_MULTIPART_FORM_DATA.toMediaType()
                file?.let {
                    requestBody =file.asRequestBody(mediaType)
                    val uploadRequestBody=UploadRequestBody(requestBody, file, index, size, mOnUpLoadFileListener)
                    val part: MultipartBody.Part = MultipartBody.Part.createFormData(key, file.name, uploadRequestBody)
                    fileList.add(part)
                    index++
                }
            }
        }

        return fileList
    }

    /**
     * 处理接口
     * @return String
     */
    private fun disposeApiUrl(): String {
        if (TextUtils.isEmpty(apiUrl)){
            apiUrl=""
        }
        return apiUrl as String
    }



    /**
     * 获取基础URL
     * @return String?
     */
    private fun getBaseUrl()=if(TextUtils.isEmpty(baseUrl)) RxHttpConfigure.getInstance().getBaseUrl() else baseUrl


    /**
     * 获取head 请求长度
     * @param url String
     * @return Long
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getContentLength(url: String, apiService:APIService): Long {
        val response = apiService.head(url).execute()
        if (response.isSuccessful) {
            val contentLength = response.headers()["Content-Length"]
            if (!TextUtils.isEmpty(contentLength)) {
                return contentLength!!.toLong()
            }
        }
        return -1
    }
}