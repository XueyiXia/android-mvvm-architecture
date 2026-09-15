package com.framework.mvvm.base

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.framework.mvvm.utils.inflateBindingWithGeneric
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmActivity <BINDING: ViewDataBinding,VM: BaseViewModel> : BaseActivity<BINDING>(){
    companion object{
        private const val TAG = "BaseMvvmActivity"
    }

}