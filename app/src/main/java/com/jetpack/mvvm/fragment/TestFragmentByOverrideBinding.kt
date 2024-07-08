package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.View
import com.framework.mvvm.base.BaseMvvmFragmentByOverrideBinding
import com.framework.mvvm.utils.viewBinding
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.ActivitySplashBinding

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class TestFragmentByOverrideBinding :BaseMvvmFragmentByOverrideBinding<ActivitySplashBinding,BaseViewModel>(){

    override val mViewDataBinding: (ActivitySplashBinding) by viewBinding(ActivitySplashBinding::inflate)


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
    }
}