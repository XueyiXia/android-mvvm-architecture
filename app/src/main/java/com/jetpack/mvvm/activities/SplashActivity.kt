package com.jetpack.mvvm.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.framework.mvvm.base.BaseMvvmActivity
import com.jetpack.mvvm.databinding.ActivitySplashBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity  : BaseMvvmActivity<ActivitySplashBinding,SplashViewModel>(){

    private var index:Int=0

    override fun createObserver(){
        mViewModel.data.observe(this, Observer {
            mViewModel.title.set(it)
            Log.e("SplashActivity", "it--->>$it   ,index-->>$index++")

        })
    }
    override fun initView(rootView: View, savedInstanceState: Bundle?) {
//        mViewDataBinding.splashmodel=mViewModel
        mViewDataBinding.click=ProxyClick()

        Log.e("SplashActivity", "initView--->>" + this.javaClass.simpleName)
    }


    inner class ProxyClick {

        fun toMain() {
            mViewModel.data.value="看得见风算啦${index++}"
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
            //带点渐变动画

            val bundle=Bundle()
            bundle.putInt("0",5)
            startActivityForResult(MainActivity::class.java,bundle,object :ActivityResultCallback<ActivityResult>{

                override fun onActivityResult(result: ActivityResult) {
                    Log.e("ActivityForResult", "result--->>111$result")
                }
            })

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }


}