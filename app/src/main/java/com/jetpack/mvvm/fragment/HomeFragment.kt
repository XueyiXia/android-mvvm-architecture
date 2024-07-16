package com.jetpack.mvvm.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.framework.mvvm.base.BaseMvvmFragment
import com.framework.mvvm.viewmodel.BaseViewModel
import com.jetpack.mvvm.AppLoader
import com.jetpack.mvvm.activities.MainActivity
import com.jetpack.mvvm.activities.TestActivity
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

        Log.e("onViewCreated+++++++", "HomeFragment")
    }

}