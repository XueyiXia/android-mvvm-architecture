package com.jetpack.mvvm.bindingadapter

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.adapter.news.NewsAdapter
import com.jetpack.mvvm.bean.news.NewsListBean

/**
 * @author: xiaxueyi
 * @date: 2025-03-28
 * @time: 14:58
 * @说明:
 */
object NewsBindingAdapter {


    //Theme Model Color Bg
    @JvmStatic
    @BindingAdapter("headerColor", requireAll = false)
    fun View.setHeaderColor(headerColor : Int) {
        this.setBackgroundColor(headerColor)
    }

    @JvmStatic
    @BindingAdapter("newsListData", requireAll = false)
    fun RecyclerView.setData(dataList: MutableLiveData<MutableList<NewsListBean.Issue.Item>?>) {
        Log.e("RecyclerView++","$dataList")
        dataList.value?.let {
            if (this.adapter is NewsAdapter){
                val newsAdapter =adapter as NewsAdapter
                newsAdapter.submitData(it.toList().toMutableList())
            }
        }



    }
}