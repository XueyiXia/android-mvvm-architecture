package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.mvvm.base.BaseFragment
import com.framework.mvvm.base.BaseMvvmFragment
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.databinding.FragmentMallsBinding
import com.jetpack.mvvm.utils.MvvmSCUtils
import com.jetpack.mvvm.viewmodel.DeviceViewModel
import com.jetpack.mvvm.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class MallsFragment : BaseFragment<FragmentMallsBinding>(){


    private val viewModel by activityViewModel<DeviceViewModel>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMallsBinding {
        return FragmentMallsBinding.inflate(inflater, container, false)
    }


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        this.mBinding.setVariable(BR.wifiViewModel, this.viewModel)
        Log.e("onViewCreated+++++++", "MallsFragment")

        viewModel.startWifiMeasure()
    }
}