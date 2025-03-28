package com.framework.http.http

import com.framework.http.utils.HttpConstants
import com.framework.http.utils.SSLUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 15:56
 * @说明:
 */

object OkHttpClientUtils {
    private const val TAG="OkHttpClientUtils"
    fun getOkHttpClientBuild(): OkHttpClient {

        //设置日志等级
        val httpLoggingInterceptor = HttpLoggingInterceptor();
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder().apply {
            connectTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            readTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
        }

        try {
            val sslParams= SSLUtils.getSSLSocketFactory(certificates = ArrayList())
            builder.sslSocketFactory(sslParams.first,sslParams.second)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        return builder.build()
    }
}