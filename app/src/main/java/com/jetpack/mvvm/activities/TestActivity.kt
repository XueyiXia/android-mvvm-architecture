package com.jetpack.mvvm.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.framework.mvvm.base.BaseMvvmActivityByOverrideBinding
import com.framework.mvvm.utils.viewBinding
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.ActivitySplashBinding
import com.jetpack.mvvm.utils.SC

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TestActivity: BaseMvvmActivityByOverrideBinding<ActivitySplashBinding,BaseViewModel>(){

    override val mViewDataBinding: (ActivitySplashBinding) by viewBinding(ActivitySplashBinding::inflate)


    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        mViewDataBinding.welcomeImage.setOnClickListener {
            val intent= Intent()
            val bundle=Bundle()
            bundle.putInt("onDestroy",10)
            bundle.putInt("onDestroy2",20)
            bundle.putInt("onDestroy3",30)
            intent.putExtra(SC.commonResultCode,bundle)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }

}