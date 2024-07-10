package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.View
import com.framework.mvvm.base.BaseMvvmFragmentByOverrideBinding
import com.framework.mvvm.utils.viewBinding
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.databinding.ActivitySplashBinding
import com.jetpack.mvvm.databinding.FragmentAddressBookBinding

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class AddressBookFragment :BaseMvvmFragmentByOverrideBinding<FragmentAddressBookBinding,BaseViewModel>(){

    override val mViewDataBinding: (FragmentAddressBookBinding) by viewBinding(FragmentAddressBookBinding::inflate)


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
    }
}