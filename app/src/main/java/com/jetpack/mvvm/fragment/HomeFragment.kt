package com.jetpack.mvvm.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.framework.mvvm.base.BaseMvvmFragment
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.AppLoader
import com.jetpack.mvvm.activities.MainActivity
import com.jetpack.mvvm.databinding.FragmentHomeBinding
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class HomeFragment :BaseMvvmFragment<FragmentHomeBinding, SplashViewModel>(){

    private var index:Int=0

    override fun createObserver(){
        mViewModel.clickData.observe(viewLifecycleOwner, Observer {
            mViewModel.titleData.value=it
            Log.e("HomeFragment", "createObserver  it--->>$it")

        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mViewDataBinding.onclick = ProxyClick()
        Log.e("HomeFragment", "initView：   ViewModel--->>$mViewModel    rootView--->>$rootView    savedInstanceState--->>$savedInstanceState ")
    }



    inner class ProxyClick {
        fun updateUi() {
            mViewModel.clickData.value="看得见风算啦${index++}"
        }
    }

}