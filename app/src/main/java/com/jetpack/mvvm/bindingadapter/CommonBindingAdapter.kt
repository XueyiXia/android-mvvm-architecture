package com.jetpack.mvvm.bindingadapter

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter

/**
 * @author: xiaxueyi
 * @date: 2023-11-07
 * @time: 14:58
 * @说明:
 */
object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("titleBg", requireAll = false)
    fun View.setBackgroundColor(headerColor : Int) {
        this.setBackgroundColor(headerColor)
    }

}