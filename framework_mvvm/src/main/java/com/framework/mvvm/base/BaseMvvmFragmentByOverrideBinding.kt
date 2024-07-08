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

abstract class BaseMvvmFragmentByOverrideBinding<DB: ViewDataBinding,VM: BaseViewModel> : BaseFragment<VM>() {

    override fun createDataBinding(): View {
        return mViewDataBinding.root
    }

    override fun createObserver() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        mViewDataBinding.unbind()
    }


    abstract val mViewDataBinding:DB;
}