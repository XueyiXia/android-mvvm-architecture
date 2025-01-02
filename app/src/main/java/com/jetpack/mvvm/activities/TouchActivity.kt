package com.jetpack.mvvm.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.framework.mvvm.base.BaseMvvmActivity
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.R
import com.jetpack.mvvm.databinding.ActivityCudaBinding
import com.jetpack.mvvm.databinding.ActivityTouchBinding
import com.jetpack.mvvm.viewmodel.CommonViewModel

/**
 * @author:
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TouchActivity: BaseMvvmActivity<ActivityTouchBinding, CommonViewModel>(){



    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        this.mBinding.setVariable(BR.CommonViewModel,this.mViewModel)

        mBinding.btnClick.setOnClickListener {

            Log.d("btnClick","btnClick")
            Toast.makeText(this,"可以点击",Toast.LENGTH_LONG).show()
        }

    }



}