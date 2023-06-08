package com.framework.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.getVmClazz
import com.framework.mvvm.utils.inflateBindingWithGeneric
import com.framework.mvvm.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmFragment<DB: ViewDataBinding,VM: BaseViewModel> :BaseFragment<VM>() {

    lateinit var mViewDataBinding:DB;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding  = inflateBindingWithGeneric(inflater,container,false)
        return mViewDataBinding.root
    }


    override fun createDataBinding(): View {
        return mViewDataBinding.root
    }

    override fun createObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewDataBinding.unbind()
    }
}