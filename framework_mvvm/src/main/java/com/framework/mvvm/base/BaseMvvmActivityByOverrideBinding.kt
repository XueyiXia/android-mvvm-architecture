package com.framework.mvvm.base

import android.view.View
import androidx.databinding.ViewDataBinding
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmActivityByOverrideBinding <DB: ViewDataBinding,VM: BaseViewModel> : BaseActivity<VM>(){
    companion object{
        private const val TAG = "BaseMvvmActivity"
    }


    override fun createDataBinding(): View {
        return mViewDataBinding.root
    }

    override fun createObserver() {

    }


    abstract val mViewDataBinding: DB
}