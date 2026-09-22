package com.jetpack.mvvm.bindingadapter

import android.view.View
import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.framework.mvvm.event.SingleLiveEvent
import com.framework.mvvm.livedata.IntLiveData

/**
 * @author: xiaxueyi
 * @date: 2023-11-07
 * @time: 14:58
 * @说明:
 */
object BleBindingAdapter {

    @JvmStatic
    @BindingAdapter("titleBg", requireAll = false)
    fun CoordinatorLayout.setBgColor(headerColor : Int) {
        this.setBackgroundColor(headerColor)
    }


    @JvmStatic
    @BindingAdapter("onBindingScanListener", requireAll = false)
    fun Button.onBindingScanListener(onBindingScanListener : IntLiveData) {
        this.setOnClickListener {
            onBindingScanListener.value=0
        }
    }


    @JvmStatic
    @BindingAdapter("onMeasureClickListener", requireAll = false)
    fun Button.onMeasureClickListener(onMeasureClickListener : MutableLiveData<View>) {
        this.setOnClickListener {
            onMeasureClickListener.value=it
        }

    }


    @JvmStatic
    @BindingAdapter("onConnectionClickListener", requireAll = false)
    fun Button.onConnectionClickListener(clickListener : MutableLiveData<View>) {
        this.setOnClickListener {
            clickListener.value=it
        }

    }
}