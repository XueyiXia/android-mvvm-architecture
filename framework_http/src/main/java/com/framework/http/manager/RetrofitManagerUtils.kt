package com.framework.http.manager

import com.framework.http.http.OkHttpClientUtils
import com.framework.http.utils.HttpConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 15:20
 * @说明:
 */

class RetrofitManagerUtils private constructor() {

    private lateinit var mRetrofit: Retrofit

    companion object{
//       private val mInstance : RetrofitManagerUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
//            RetrofitManagerUtils()
//        }

        fun getInstance():RetrofitManagerUtils{
            val instance : RetrofitManagerUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
                RetrofitManagerUtils()
            }
            return instance
        }
    }






    fun getRetrofit(baseUrl:String):Retrofit{
        //创建json
        val gson: Gson = GsonBuilder()
            .setDateFormat(HttpConstants.DATE_FORMAT)
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();


        /**
         * Retrofit
         */
        mRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClientUtils.getOkHttpClientBuild())
            .build()

        return mRetrofit
    }


    /**
     *
     * @param tClass Class<T>
     * @return T
     */
    fun <T> createService(tClass: Class<T>):T{
        return mRetrofit.create(tClass)
    }
}