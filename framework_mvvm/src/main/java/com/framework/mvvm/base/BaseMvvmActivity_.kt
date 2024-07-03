package com.framework.mvvm.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.framework.mvvm.R
import com.framework.mvvm.utils.inflateBindingWithGeneric
import com.framework.mvvm.viewmodel.BaseViewModel
import com.google.android.material.color.DynamicColors

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmActivity_ <DB: ViewDataBinding> : AppCompatActivity(){
    companion object{
        private const val TAG = "BaseMvvmActivity"
    }

    abstract val mViewDataBinding: DB

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        setContentView(mViewDataBinding.root)

    }
}