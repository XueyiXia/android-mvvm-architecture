package com.jetpack.mvvm.ui.news.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.framework.http.http.RxHttp
import com.framework.http.interfac.SimpleResponseListener
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.bean.news.NewsListBean
import com.rxjava_retrofit.HttpApi
import java.util.TreeMap


class NewsViewModel() : BaseViewModel(){


    companion object{
        private const val TAG="NewsViewModel";
    }

    private var current=1

    val newsList = MutableLiveData<MutableList<NewsListBean.Issue.Item>>()

    private var parameter = TreeMap<String,Any>().apply {
        this["num"] = current.toString()
    }


    fun refreshData(context:FragmentActivity){
        current=1
        parameter.apply {
            this["num"]=current
        }

        callNewsListApi(context)
    }

    fun loadMoreData(context:FragmentActivity){
        current++
        parameter.apply {
            this["num"]=current
        }

        callNewsListApi(context)
    }

    private fun callNewsListApi(context:FragmentActivity){

        RxHttp.getInstance()
            .setApiUrl(HttpApi.test)
            .setLifecycle(context)
            .setParameter(parameter)
            .setContext(context)
            .get()
            .build()
            .execute(object : SimpleResponseListener<NewsListBean>() {
                override fun onSucceed(data: NewsListBean, method: String) {
                    super.onSucceed(data, method)
                    Log.e(TAG,"输出的数据(onSuccess)${data}")
                    newsList.postValue(data.issueList[0].itemList.toMutableList())
                }

                override fun onCompleted() {
                    super.onCompleted()
                    Log.e(TAG,"输出的数据(onCompleted)")
                }

                override fun onError(e: Throwable?) {
                    super.onError(e)
                    Log.e(TAG,"(onError)${e}")
                }
            })
    }
}
