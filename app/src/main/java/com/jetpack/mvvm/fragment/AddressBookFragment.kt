package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.mvvm.base.BaseMvvmFragment
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.FragmentAddressBookBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class AddressBookFragment : BaseMvvmFragment<FragmentAddressBookBinding, BaseViewModel>(){


    private val viewModel by activityViewModel<SplashViewModel>()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddressBookBinding {
        return FragmentAddressBookBinding.inflate(inflater, container, false)
    }


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mBinding.title.text="测试 WebView"
        mBinding.webView.loadUrl("https://github.com/getActivity/XXPermissions")
    }
}