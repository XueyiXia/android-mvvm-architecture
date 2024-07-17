package com.jetpack.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.framework.mvvm.base.BaseMvvmFragment
import com.jetpack.mvvm.activities.TestActivity
import com.jetpack.mvvm.databinding.FragmentUserBinding
import com.jetpack.mvvm.utils.MvvmSCUtils
import com.jetpack.mvvm.viewmodel.SplashViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class UserFragment :BaseMvvmFragment<FragmentUserBinding, SplashViewModel>(){

    private val resultLauncher: ActivityResultLauncher<Intent> =  registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        val code = result.resultCode
        val data = result.data

        val bundle=result.data?.extras
       val resul= bundle?.getInt("onDestroy3",88)
        val msgContent = "result = $result   ，bundle==$bundle   code = $code  ,data : $data   , 88 = ${result.data?.getIntExtra(MvvmSCUtils.commonResultCode,-1)}  "
        Log.e("launcherCallback","msgContent: ->> $msgContent")

    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        mViewDataBinding.next.setOnClickListener {
            val intent = Intent(requireActivity(), TestActivity::class.java)
            resultLauncher.launch(intent)
        }
    }
}