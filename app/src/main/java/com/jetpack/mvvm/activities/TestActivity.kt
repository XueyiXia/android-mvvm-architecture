package com.jetpack.mvvm.activities

import android.os.Bundle
import android.view.View
import com.framework.mvvm.base.BaseMvvmActivityByOverrideBinding
import com.framework.mvvm.utils.viewBinding
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.ActivitySplashBinding

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TestActivity: BaseMvvmActivityByOverrideBinding<ActivitySplashBinding,BaseViewModel>(){

    override val mViewDataBinding: (ActivitySplashBinding) by viewBinding(ActivitySplashBinding::inflate)


    override fun initView(rootView: View, savedInstanceState: Bundle?) {

    }

}