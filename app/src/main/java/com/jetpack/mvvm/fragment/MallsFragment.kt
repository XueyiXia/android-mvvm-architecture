package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.databinding.FragmentMallsBinding
import com.jetpack.mvvm.utils.MvvmSCUtils

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class MallsFragment : BaseFragment<FragmentMallsBinding>(){
    override fun bindDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentMallsBinding {
        return FragmentMallsBinding.inflate(inflater,container,attachToParent)
    }


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        Log.e("onViewCreated+++++++", "MallsFragment")

        mBinding.next.setOnClickListener {
            startActivityForResult(TestActivity::class.java){ activityResult->
                Log.e("launcherCallback666","activityResult: ->> $activityResult")
                val bundleResult=activityResult.data?.getBundleExtra(MvvmSCUtils.commonResultCode)
                val key1=bundleResult?.getInt(MvvmSCUtils.key1,-1)
                val key2=bundleResult?.getString(MvvmSCUtils.key2,)
                val key3=bundleResult?.getCharSequenceArrayList(MvvmSCUtils.key3)
                Log.e("launcherCallback666","key1: ->> $key1   , key2: ->> $key2  ,  key3: ->> $key3 ")
            }
        }




        mBinding.nextHasParams.setOnClickListener {

            val bundle: Bundle = Bundle()
            bundle.putInt(MvvmSCUtils.key1,100)
            bundle.putString(MvvmSCUtils.key2,"字符串")
            bundle.putCharSequenceArrayList(MvvmSCUtils.key3, arrayListOf("1","2"))


            startActivityForResult(TestActivity::class.java,bundle){ activityResult->
                Log.e("launcherCallback666","activityResult: ->> $activityResult")
                val bundleResult=activityResult.data?.getBundleExtra(MvvmSCUtils.commonResultCode)
                val key1=bundleResult?.getInt(MvvmSCUtils.key1,-1)
                val key2=bundleResult?.getString(MvvmSCUtils.key2,)
                val key3=bundleResult?.getCharSequenceArrayList(MvvmSCUtils.key3)
                Log.e("launcherCallback666","key1: ->> $key1   , key2: ->> $key2  ,  key3: ->> $key3 ")
            }
        }


        mBinding.next1.setOnClickListener{
            val bundle: Bundle = Bundle()
            bundle.putInt(MvvmSCUtils.key1,100)
            bundle.putString(MvvmSCUtils.key2,"字符串")
            startActivity(TestActivity::class.java,bundle)
        }
    }
}