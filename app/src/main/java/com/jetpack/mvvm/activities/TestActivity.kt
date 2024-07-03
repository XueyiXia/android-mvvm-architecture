package com.jetpack.mvvm.activities

import android.os.Bundle
import com.framework.mvvm.base.BaseMvvmActivity_
import com.framework.mvvm.utils.viewBinding
import com.jetpack.mvvm.databinding.ActivitySplashBinding

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TestActivity: BaseMvvmActivity_<ActivitySplashBinding>(){

    override val mViewDataBinding: (ActivitySplashBinding) by viewBinding(ActivitySplashBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.tvCount.text="testx++++++"
    }
}