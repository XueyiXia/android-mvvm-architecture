package com.framework.mvvm.base

import androidx.databinding.ViewDataBinding
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmFragment<BINDING: ViewDataBinding,VM: BaseViewModel> :BaseFragment<BINDING>(){


}