package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.View
import com.framework.mvvm.base.BaseMvvmFragment
import com.jetpack.mvvm.activities.CudaActivity
import com.jetpack.mvvm.databinding.FragmentHomeBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class HomeFragment :BaseMvvmFragment<FragmentHomeBinding, SplashViewModel>(){


    override fun initView(rootView: View, savedInstanceState: Bundle?) {


        mViewDataBinding.click.setOnClickListener {
            startActivity(CudaActivity::class.java)
        }
    }

}