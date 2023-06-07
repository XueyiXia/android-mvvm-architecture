package com.jetpack.mvvm.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.framework.mvvm.base.BaseActivity
import com.framework.mvvm.databind.StringObservableField
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.ActivitySplashBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity  : BaseActivity<ActivitySplashBinding,SplashViewModel>(){

    private val splashViewModel: SplashViewModel by viewModels()

    override fun createObserver() {
        splashViewModel.name.observe(this, Observer {
            splashViewModel.name.setValue(StringObservableField("dasdsdr"))
        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mViewDataBinding.click=ProxyClick()

    }


    inner class ProxyClick {
        fun toMain() {
            splashViewModel.name.value?.notifyChange()
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
            //带点渐变动画
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}