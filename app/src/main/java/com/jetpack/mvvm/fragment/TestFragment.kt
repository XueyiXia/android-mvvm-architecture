package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.framework.mvvm.base.BaseMvvmFragment_
import com.framework.mvvm.utils.viewBinding
import com.jetpack.mvvm.R
import com.jetpack.mvvm.databinding.ActivitySplashBinding

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class TestFragment :BaseMvvmFragment_<ActivitySplashBinding>(){

    override val mViewDataBinding: (ActivitySplashBinding) by viewBinding(ActivitySplashBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        mViewDataBinding.tvCount.text="ssssss"
    }
}