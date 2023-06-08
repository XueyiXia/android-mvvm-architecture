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

    override fun createObserver() {
        mViewModel.name.notifyChange()
        Log.e("HomeFragment", "createObserver--->> ViewModel--->>$mViewModel ")
//        mViewModel.name.observe(viewLifecycleOwner, Observer {
//            Log.e("HomeFragment", "it--->>$it" )
//            mViewDataBinding.next.text=it
//        })
//
//        mViewModel.bgColor.observe(viewLifecycleOwner, Observer {
//            Log.e("HomeFragment", "it--->>$it" )
//            mViewModel.bgColor.value=it
//
//        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mViewDataBinding.onclick = ProxyClick()
//        mViewModel.name.value="test+++++ "
//        mViewModel.bgColor.value=Color.YELLOW
        mViewModel.name.set("dadadasd")
        Log.e("HomeFragment", "initView：   ViewModel--->>$mViewModel    rootView--->>$rootView    savedInstanceState--->>$savedInstanceState ")
    }



    inner class ProxyClick {
        fun updateUi() {
            mViewModel.name.set("dadadasd")
            mViewModel.name.notifyChange()
//            mViewModel.bgColor.set(00000)
        }
    }

}