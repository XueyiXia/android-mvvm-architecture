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

    override fun createObserver(){


    }
    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        Log.e("SplashActivity", "initView--->>" + this.javaClass.simpleName)

    }


    inner class ProxyClick {

        fun toMain() {
//            mViewModel.clickData.value="这是点击的数据测试 ${index++}"
            startActivity(Intent(this@SplashMvvmActivity, TestActivity::class.java))
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
            //带点渐变动画

//            val bundle=Bundle()
//            bundle.putInt("0",5)
//            startActivityForResult(MainActivity::class.java,bundle,object :ActivityResultCallback<ActivityResult>{
//
//                override fun onActivityResult(result: ActivityResult) {
//                    Log.e("ActivityForResult", "result--->>111$result")
//                }
//            })

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }


}