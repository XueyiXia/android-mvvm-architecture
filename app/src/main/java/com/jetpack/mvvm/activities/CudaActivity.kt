package com.jetpack.mvvm.activities

import android.os.Bundle
import android.view.View
import com.framework.mvvm.base.BaseMvvmActivity
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.R
import com.jetpack.mvvm.databinding.ActivityCudaBinding
import com.jetpack.mvvm.viewmodel.CommonViewModel

/**
 * @author:
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class CudaActivity: BaseMvvmActivity<ActivityCudaBinding, CommonViewModel>(){



    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        this.mBinding.setVariable(BR.CommonViewModel,this.mViewModel)
        rootView.setBackgroundColor(this.resources.getColor(R.color.purple_200,null))


    }



}