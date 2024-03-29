package com.jetpack.mvvm.bindingadapter

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter

/**
 * @author: xiaxueyi
 * @date: 2023-11-07
 * @time: 14:58
 * @说明:
 */
object SplashBindingAdapter {

    @JvmStatic
    @BindingAdapter("titleBg", requireAll = false)
    fun CoordinatorLayout.setBgColor(headerColor : Int) {
        this.setBackgroundColor(headerColor)
    }

}