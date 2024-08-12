package com.jetpack.mvvm.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.framework.mvvm.base.BaseMvvmActivity
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.R
import com.jetpack.mvvm.databinding.ActivitySplashBinding
import com.jetpack.mvvm.databinding.ActivityTestBinding
import com.jetpack.mvvm.utils.MvvmSCUtils
import com.jetpack.mvvm.viewmodel.CommonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author: xiaxueyi
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TestActivity: BaseMvvmActivity<ActivityTestBinding, CommonViewModel>(){

//    override val mViewDataBinding: (ActivitySplashBinding) by viewBinding(ActivitySplashBinding::inflate)

    private val viewModel by viewModel<CommonViewModel>()

    private val mBundle=Bundle()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        this.mBinding.setVariable(BR.CommonViewModel,this.mViewModel)
        val bundle=intent.extras
        if (bundle!=null){
            val key1=bundle.getInt(MvvmSCUtils.key1,-1)
            val key2=bundle.getString(MvvmSCUtils.key2,)
            val key3=bundle.getCharSequenceArrayList(MvvmSCUtils.key3)
            Log.e("TestActivity+++","key1: ->> $key1   , key2: ->> $key2  ,  key3: ->> $key3 ")
        }


        Log.e("TestActivity+++","initView: ->> null ")

        mBinding.tvCount.setOnClickListener{

            val bundle2: Bundle = Bundle()
            bundle2.putInt(MvvmSCUtils.key1,9858)
            bundle2.putString(MvvmSCUtils.key2,"跳转到测试页面")
            bundle2.putCharSequenceArrayList(MvvmSCUtils.key3, arrayListOf("1跳转到测试页面","2跳转到测试页面"))

            startActivityForResult(TestActivity::class.java,bundle2){ activityResult->
                Log.e("launcherCallback666","activityResult: ->> $activityResult")
                val bundleResult=activityResult.data?.getBundleExtra(MvvmSCUtils.commonResultCode)
                val key1=bundleResult?.getInt(MvvmSCUtils.key1,-1)
                val key2=bundleResult?.getString(MvvmSCUtils.key2,)
                val key3=bundleResult?.getCharSequenceArrayList(MvvmSCUtils.key3)
                Log.e("launcherCallback666","key1: ->> $key1   , key2: ->> $key2  ,  key3: ->> $key3 ")
            }
        }

        mBinding.welcomeImage.setOnClickListener {
            if (bundle!=null){

                val intent= Intent()
                mBundle.putInt(MvvmSCUtils.key1,10)
                mBundle.putString(MvvmSCUtils.key2,"100")
                mBundle.putCharSequenceArrayList(MvvmSCUtils.key3, arrayListOf("100","6000"))
                intent.putExtra(MvvmSCUtils.commonResultCode,bundle)
                setResult(Activity.RESULT_OK,intent)
                this.finish()
            }else{
                val intent= Intent()
                mBundle.putInt(MvvmSCUtils.key1,1000000)
                mBundle.putString(MvvmSCUtils.key2,"蚕食为空跳转")
                mBundle.putCharSequenceArrayList(MvvmSCUtils.key3, arrayListOf("100","参数为空"))
                intent.putExtra(MvvmSCUtils.commonResultCode,mBundle)
                setResult(Activity.RESULT_OK,intent)
                this.finish()
            }

        }

    }


    override fun onResume() {
        super.onResume()
        Log.e("TestActivity+++","onResume: ->> null ")
        mBinding.welcomeImage.setImageResource(R.drawable.ic_wx_back_pressed)
    }
}