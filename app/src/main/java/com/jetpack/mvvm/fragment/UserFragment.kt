package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.databinding.FragmentUserBinding

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class UserFragment : BaseFragment<FragmentUserBinding>(){
    override fun bindDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater,container,attachToParent)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        mBinding.next.setOnClickListener {
            val bundle: Bundle = Bundle()
            startActivity(TestActivity::class.java,bundle)
        }
    }
}