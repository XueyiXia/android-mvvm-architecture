package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.activities.RefreshActivity
import com.jetpack.mvvm.activities.TouchActivity
import com.jetpack.mvvm.databinding.FragmentHomeBinding

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class HomeFragment :BaseFragment<FragmentHomeBinding>(){
    override fun bindDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }


    override fun initView(rootView: View, savedInstanceState: Bundle?) {



        mBinding.touchLayout.setOnClickListener {
            startActivity(TouchActivity::class.java)
        }


        mBinding.refreshLayout.setOnClickListener {
            startActivity(RefreshActivity::class.java)
        }
    }

}