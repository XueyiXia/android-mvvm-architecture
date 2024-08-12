package com.jetpack.mvvm.bindingadapter

import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter

/**
 * @author: xiaxueyi
 * @date: 2023-11-07
 * @time: 14:58
 * @说明:
 */
object CommonBindingAdapter {


    //Theme Model Color Bg
    @JvmStatic
    @BindingAdapter("headerColor", requireAll = false)
    fun View.setHeaderColor(headerColor : Int) {
        this.setBackgroundColor(headerColor)
    }
}