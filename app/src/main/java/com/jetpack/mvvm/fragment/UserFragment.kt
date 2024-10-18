package com.jetpack.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.framework.mvvm.base.BaseMvvmFragment
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.bean.UserInfoBean
import com.jetpack.mvvm.databinding.FragmentUserBinding
import com.jetpack.mvvm.utils.MvvmSCUtils
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class UserFragment :BaseMvvmFragment<FragmentUserBinding, SplashViewModel>(){

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        mViewDataBinding.next.setOnClickListener {
            val bundle: Bundle = Bundle()
//            bundle.putBinder(MvvmSCUtils.PARAM_BUNDLE,UserInfoBean)
            startActivity(TestActivity::class.java,bundle)
        }
    }
}