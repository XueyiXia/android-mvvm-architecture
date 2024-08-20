package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.framework.mvvm.base.BaseMvvmFragment
import com.framework.mvvm.base.BaseMvvmFragmentByOverrideBinding
import com.framework.mvvm.utils.viewBinding
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.ActivitySplashBinding
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

//    override val mViewDataBinding: (FragmentAddressBookBinding) by viewBinding(FragmentAddressBookBinding::inflate)


    private val viewModel by activityViewModel<SplashViewModel>()


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mViewDataBinding.title.text="测试 WebView"
        mViewDataBinding.webView.loadUrl("https://github.com/getActivity/XXPermissions")
    }
}