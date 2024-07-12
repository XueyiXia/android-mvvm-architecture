package com.jetpack.mvvm.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.framework.mvvm.base.BaseMvvmActivity
import com.jetpack.mvvm.databinding.ActivitySplashBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class SplashMvvmActivity  : BaseMvvmActivity<ActivitySplashBinding,SplashViewModel>(){


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        Log.e("SplashActivity", "initView--->>" + this.javaClass.simpleName)


        startActivity(Intent(this@SplashMvvmActivity, MainActivity::class.java))
        finish()
    }


    inner class ProxyClick {

    }


}