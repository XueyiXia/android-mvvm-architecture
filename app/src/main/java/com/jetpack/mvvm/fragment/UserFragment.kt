package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.mvvm.base.BaseMvvmFragment
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.databinding.FragmentUserBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class UserFragment :BaseMvvmFragment<FragmentUserBinding, SplashViewModel>(){


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        mBinding.next.setOnClickListener {
            val bundle: Bundle = Bundle()
//            bundle.putBinder(MvvmSCUtils.PARAM_BUNDLE,UserInfoBean)
            startActivity(TestActivity::class.java,bundle)
        }
    }
}