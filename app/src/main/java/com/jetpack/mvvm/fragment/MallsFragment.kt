package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.framework.mvvm.base.BaseFragment
import com.framework.mvvm.base.BaseMvvmFragment
import com.jetpack.mvvm.R
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.databinding.FragmentHomeBinding
import com.jetpack.mvvm.databinding.FragmentMallsBinding
import com.jetpack.mvvm.utils.SC
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class MallsFragment : BaseMvvmFragment<FragmentMallsBinding, SplashViewModel>(){


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        Log.e("onViewCreated+++++++", "MallsFragment")

        mViewDataBinding.next.setOnClickListener {
            startActivityForResult(TestActivity::class.java){ activityResult->
                Log.e("launcherCallback666","it: ->> ${activityResult}")

                val shareResult = activityResult.data?.getIntExtra(
                    SC.commonResultCode,
                    0
                )

                Log.e("launcherCallback666","shareResult: ->> $shareResult")
            }
        }
    }
}