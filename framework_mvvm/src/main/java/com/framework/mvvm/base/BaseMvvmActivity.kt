package com.framework.mvvm.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.getVmClazz
import com.framework.mvvm.utils.inflateBindingWithGeneric
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmActivity <DB: ViewDataBinding,VM: BaseViewModel> : BaseActivityNew<DB>(){
    companion object{
        private const val TAG = "BaseMvvmActivity"
    }

    lateinit var mViewModel: VM


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        /**
         * 实例化创建ViewModel
         */
        createViewModel()
    }

    /**
     * 创建ViewModel
     * @return VM
     */
    private fun createViewModel() {
        val modelClass :Class<VM> = getVmClazz(this)
//        val observer=createObserver()
        mViewModel= ViewModelProvider(this)[modelClass]
    }

    /**
     * 创建LiveData数据观察者
     */
//    abstract fun createObserver():VM



}